package com.wallaby.moamoa.security.filter;

import com.wallaby.moamoa.common.exception.CustomException;
import com.wallaby.moamoa.common.util.authorize.AuthorizationInfo;
import com.wallaby.moamoa.common.util.authorize.AuthorizationInfoRegistry;
import com.wallaby.moamoa.security.JwtTokenManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final String ACCESS_TOKEN_HEADER_NAME = "Authorization";
    private final AuthorizationInfoRegistry authorizationInfoRegistry;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final JwtTokenManager jwtTokenManager;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Authentication Start");
        log.debug("Request URI : {}", request.getRequestURI());
//        log.debug("ip : {}", request.getRemoteAddr());

        // 토큰 검증이 필요한 요청인지 확인
        if (!isRequiredAuthentication(request.getRequestURI(), request.getMethod())) {
            filterChain.doFilter(request, response); // 필요하지 않다면 바로 Controller로 넘어가 API 수행
            return;
        }

        // 헤더에서 Access 토큰을 가져옴
        log.info("Get authenticationValue from header");
        String authenticationValue = request.getHeader(ACCESS_TOKEN_HEADER_NAME);

        try {
            // 토큰 검증 시작
            log.info("Try Authentication JWT token");
            String accessToken = jwtTokenManager.parseAndValidateAuthorizationValue(authenticationValue, request);

            // 토큰 검증
            accessTokenValidate(request, accessToken);

            // 리프레쉬 토큰 검증
            checkSignOutAndValidateRefreshToken(accessToken, request);

            // 모든 검증이 통과 되면 인증 객체를 만들어냄
            Authentication authentication = jwtTokenManager.getAuthentication(accessToken);

            // 인증 객체를 Security에 전달하여 저장
            // 이 과정으로 유효한 요청이라는 검증이 완료 되고, 해당 내용을 SecurityContextHolder에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 요청을 수행
            log.info("Success Authentication JWT token, process next filter");
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            // 예외 발생 시 인증 과정중에 문제가 발생했다는 응답을 보냄
//            ResponseMaker.makeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getErrorCode());
        }
    }

    private void accessTokenValidate(HttpServletRequest request, String accessToken) {
        boolean isNotExpired = jwtTokenManager.accessTokenValidate(accessToken);

        // Access 토큰이 만료 되었다면 request 객체에 토큰을 담아 토큰이 만료된 요청이라는 것을 전달
        // 응답이 나갈 때, 이것을 바탕으로 응답 헤더에 새 토큰을 전달해 줌
        if (!isNotExpired) {
            request.setAttribute("accessTokenExpired", accessToken);
        }

        // 이전 토큰을 가져오기 위한 Redis Operations
        log.info("Get before access token");
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        // 현재 들어온 토큰에 기록 되어 있는 사용자 id를 가져옴
        String customerId = jwtTokenManager.getAccessTokenSubject(accessToken);
        log.debug("encrypted customerId: {}, decrypted customerId: {}", customerId, customerId);

        // customerId에 할당 되어 있는 access 토큰을 redis에서 가져옴
        String beforeAccessToken = valueOperations.get(customerId);
        log.debug("Before access token: {}", beforeAccessToken);

        // customerId에 할당 되어 있는 access 토큰이 없다면 이미 로그아웃 된 사용자
        if (beforeAccessToken == null) {
//            log.info("Exception occurred : errorCode={}, logMsg={}", ErrorCode.NEED_AUTHENTICATION.getCode()
//                    , ErrorCode.NEED_AUTHENTICATION.getLogMsg());
//            throw new CustomException(ErrorCode.NEED_AUTHENTICATION);
        } else if (!beforeAccessToken.equals(accessToken)) {
            // 들어오 토큰과 할당되어 있는 토큰이 일치하지 않다면 인증된 사용자가 아니기 때문에 해당 모든 세션종료
            redisTemplate.delete(customerId);   // redis에서 사용자 정보를 지움
            valueOperations.set(accessToken,"invalid"   // 3일간 해당 토큰은 사용할 수 없음
                    , Duration.ofMillis(1000 * 60 * 60 * 24 * 3));

//            log.info("Exception occurred : errorCode={}, logMsg={}", ErrorCode.INVALID_ACCESS.getCode()
//                    , ErrorCode.INVALID_ACCESS.getLogMsg());
//            throw new CustomException(ErrorCode.INVALID_ACCESS);
        }
    }

    private void checkSignOutAndValidateRefreshToken(String accessToken, HttpServletRequest request) {
        // Refresh 토큰 검증
        log.info("Refresh token validate");
        log.debug("accessToken={}", accessToken);
        // redis에 저장된 Refresh 토큰을 가져오기위한 valueOperations
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        // access 토큰을 기반으로 저장된 refresh 토큰을 가져옴
        String refreshTokenValue = valueOperations.get(accessToken);
        log.debug("refreshTokenValue={}", refreshTokenValue);

        // refresh 토큰이 있다면
        if (refreshTokenValue != null) {
            // refresh 토큰의 값이 logout 이라면 로그아웃 된 사용자
            if (refreshTokenValue.equals("logout")) {
//                log.info("Exception occurred : errorCode={}, logMsg={}", ErrorCode.ALREADY_SIGN_OUTED_CUSTOMER.getCode()
//                        , ErrorCode.ALREADY_SIGN_OUTED_CUSTOMER.getLogMsg());
//                throw new CustomException(ErrorCode.ALREADY_SIGN_OUTED_CUSTOMER);
            } else if (refreshTokenValue.equals("invalid")){
                // refresh 토큰의 값이 invalid 상태라면 접근이 제한된 사용자, 즉, 접근을 거부하는 토큰
//                log.info("Exception occurred : errorCode={}, logMsg={}", ErrorCode.RESTRICTED_TOKEN.getCode()
//                        , ErrorCode.RESTRICTED_TOKEN.getLogMsg());
//                throw new CustomException(ErrorCode.RESTRICTED_TOKEN);
            }
            else {
                // 실제 refresh 토큰을 검증
                boolean isNotExpired = jwtTokenManager.refreshTokenValidate(refreshTokenValue);

                // refresh 토큰이 만료 되었다면 request 객체에 access 토큰을 담아 토큰이 만료된 요청이라는 것을 전달
                // 응답이 나갈 때, 이것을 바탕으로 응답 헤더에 새 토큰을 전달해 줌
                if (!isNotExpired) {
                    request.setAttribute("refreshTokenExpired", accessToken);
                }
            }
        }
    }


    private boolean isRequiredAuthentication(String url, String method) {
        log.info("Check Require Authentication");
        log.debug("target url={}, method={}", url, method);

        List<AuthorizationInfo> authorizationInfoList = authorizationInfoRegistry.getObjectList();

        boolean isMatchedUrl = false;

        log.info("Find required Authentication URL");
        for (AuthorizationInfo info : authorizationInfoList) {
            if (antPathMatcher.match("/v3/api-docs/**",url) ||
                    antPathMatcher.match("/swagger-ui/**", url) ||
                    antPathMatcher.match("/swagger-ui.html",url)) {
                log.info("Swagger URL");
                return false;
            } else {
                if (antPathMatcher.match(info.getUrl(), url)
                        && method.equalsIgnoreCase(info.getMethod().name())) {
                    if (info.getRoles() == null) {
                        log.info("URL={}, method={} not required", info.getUrl(), info.getMethod());
                        return false;
                    }
                    isMatchedUrl = true;
                }
            }
        }

        if (!isMatchedUrl) {
            return false;
        }

        log.info("Target is required authentication");
        return true;
    }
}
