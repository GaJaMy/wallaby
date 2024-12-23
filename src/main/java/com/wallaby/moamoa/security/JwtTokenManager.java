package com.wallaby.moamoa.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenManager {
    private static final long ACCESS_TOKEN_EXPIRED_TIME = 1000 * 60 * 60 * 2;
    private static final long REFRESH_TOKEN_EXPIRED_TIME = 1000 * 60 * 60 * 24;
    private static final String TOKEN_TYPE = "Bearer ";
//    private static final Logger log = LoggerFactory.getLogger(JwtTokenManager.class);
    private final Key key;

    public JwtTokenManager(@Value("${jwt.secret.key}") String secretKey) {
        byte[] byteKey = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(byteKey);
    }


    private String genAccessToken(String customerId, Collection<? extends GrantedAuthority> authorities) {
        log.info("Generate access token");
        long now = System.currentTimeMillis();
        long expiredTime = now + ACCESS_TOKEN_EXPIRED_TIME;

        Date nowDate = new Date(now);
        Date expiredDate = new Date(expiredTime);
        log.debug("nowDate={}, expiredDate={}",nowDate,expiredDate);

        log.info("Create claims");
        Claims claims = Jwts.claims().setSubject(customerId);
        List<SimpleGrantedAuthority> authorityList = authorities.stream()
                .map(grantedAuthority -> new SimpleGrantedAuthority(grantedAuthority.getAuthority()))
                .collect(Collectors.toList());
        claims.put("authority",authorityList);

        log.debug("subject={}", claims.getSubject());
        log.debug("authority={}", authorityList);

        log.info("Create Access Token");
        String accessToken = Jwts.builder().signWith(this.key, SignatureAlgorithm.HS256)
                .setHeaderParam("type", "jwt")
                .setClaims(claims)
                .setIssuer("DepRx")
                .setIssuedAt(nowDate)
                .setExpiration(expiredDate)
                .compact();

        log.debug("accessToken={}",accessToken);
        log.debug("type=jwt");
        log.debug("issuer=DepRx");
        log.debug("issuedAt={}",nowDate);
        log.debug("expiredAt={}",expiredDate);

        return accessToken;
    }

    public boolean accessTokenValidate(String accessToken) {
        // 토큰 검증
        log.info("Access token validate start");

        // 토큰이 없거나 비었다면 예외 발생
        if (accessToken == null || accessToken.isEmpty()) {
//            log.info("Exception occurred : errCode={}, logMsg={}", ErrorCode.ACCESS_TOKEN_IS_EMPTY.getCode()
//                    , ErrorCode.ACCESS_TOKEN_IS_EMPTY.getLogMsg());
//            throw new CustomException(ErrorCode.ACCESS_TOKEN_IS_EMPTY);
        }

        try {
            //토큰 파싱 시작
            log.info("Try parse access token");
            log.debug("accessToken={}", accessToken);

            // 토큰 파싱
            Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(accessToken).getBody();

            // 성공이면 true 리턴
            log.info("Success access token parse");
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            // 토큰 만료가 아닌 토큰 자체에 문제가 있을 경우 예외 발생
//            log.info("Exception occurred : errorCode={}, logMsg={}", ErrorCode.ACCESS_TOKEN_INVALID.getCode()
//                    , ErrorCode.ACCESS_TOKEN_INVALID.getLogMsg());
//            throw new CustomException(ErrorCode.ACCESS_TOKEN_INVALID);
            throw new RuntimeException("");
        } catch (ExpiredJwtException e) {
            // 토큰이 만료 되었을 경우 false 반환
            log.info("Access token is expired");
            return false;
        }
    }


    public String genRefreshToken(String memberId) {
        log.info("Generate refresh token");
        long now = System.currentTimeMillis();
        long expireTime = now + REFRESH_TOKEN_EXPIRED_TIME;

        Date nowDate = new Date(now);
        Date expiredDate = new Date(expireTime);
        log.debug("nowDate={}, expiredDate={}",nowDate,expiredDate);

        String refreshToken = Jwts.builder().signWith(this.key, SignatureAlgorithm.HS256)
                .setIssuer("DepRx")
                .setSubject(memberId)
                .setIssuedAt(nowDate)
                .setExpiration(expiredDate)
                .compact();

        log.debug("refreshToken={}",refreshToken);
        log.debug("issuer=DepRx");
        log.debug("subject={}",memberId);
        log.debug("issuedAt={}",nowDate);
        log.debug("expiredAt={}",expiredDate);

        return refreshToken;
    }

    public boolean refreshTokenValidate(String refreshToken) {
        log.info("Refresh token validate");

        // refresh 토큰이 없다면, 예외 발생
        if (refreshToken.isEmpty()) {
//            log.info("Exception occurred : errorCode={}, logMsg={}",ErrorCode.REFRESH_TOKEN_IS_EMPTY.getCode()
//                    , ErrorCode.REFRESH_TOKEN_IS_EMPTY.getLogMsg());
//            throw new CustomException(ErrorCode.REFRESH_TOKEN_IS_EMPTY);
            throw new RuntimeException("");
        }

        try {
            // refresh 토큰 파싱
            log.info("Try parse refresh token");
            log.debug("refreshToken={}", refreshToken);

            Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(refreshToken).getBody();

            log.info("Success refresh token parse");
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException e) {
            // refresh 토큰 자체에 문제가 있다면 예외 발생
//            log.info("Exception occurred : errorCode={}, logMsg={}", ErrorCode.REFRESH_TOKEN_INVALID.getCode()
//                    , ErrorCode.REFRESH_TOKEN_INVALID.getLogMsg());
//            throw new CustomException(ErrorCode.REFRESH_TOKEN_INVALID);
            throw new RuntimeException("");
        } catch (ExpiredJwtException e) {
            // refresh 토큰이 만료가 되었다면 false 반환
            log.info("Refresh token is expired");
            return false;
        }
    }

    public String parseAndValidateAuthorizationValue(String authorizationValue, HttpServletRequest request) {
        log.info("Parse AuthorizationValue");
        log.debug("authorizationValue={}", authorizationValue);

        // 검증을 위한 헤더가 없거나 비었을 경우 예외 발생
        if (authorizationValue == null || authorizationValue.isEmpty()) {
//            log.info("Exception occurred : errorCode={}, logMsg={}", ErrorCode.AUTHORIZATION_HEADER_IS_EMPTY.getCode()
//                    , ErrorCode.AUTHORIZATION_HEADER_IS_EMPTY.getLogMsg());
//            throw new CustomException(ErrorCode.AUTHORIZATION_HEADER_IS_EMPTY);
        }

        // 토큰 타입 검증
        if (!authorizationValue.startsWith(TOKEN_TYPE)) {
//            log.info("Exception occurred : errorCode={}, logMsg={}"
//                    , ErrorCode.AUTHORIZATION_VALUE_TOKEN_TYPE_INVALID.getCode()
//                    , ErrorCode.AUTHORIZATION_VALUE_TOKEN_TYPE_INVALID.getLogMsg());
//            throw new CustomException(ErrorCode.AUTHORIZATION_VALUE_TOKEN_TYPE_INVALID);
        }

        // Access 토큰만을 빼냄
        return authorizationValue.substring(TOKEN_TYPE.length());
    }

    @SuppressWarnings("unchecked")
    public Authentication getAuthentication(String accessToken) {
        log.info("Get access Authentication object from access token");
        log.debug("accessToken={}", accessToken);

        Claims claims = null;
        // 먼저 토큰에서 claims를 파싱
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(accessToken);
            claims = claimsJws.getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }

        // claims에서 권한 정보를 빼내옴
        List<Map<String,String>> authority = (List<Map<String, String>>) claims.get("authority");
        String[] authorities = authority.stream().map(auth -> auth.get("role")).toArray(String[]::new);

        StringBuilder sb = new StringBuilder();
        sb.append("Authorities=");
        for (String item : authorities) {
            sb.append(item)
                    .append(" ");
        }
        log.debug(sb.toString());

        // 권한 정보 생성
        Collection<? extends GrantedAuthority> grantedAuthorities = Arrays.stream(authorities)
                .map(SimpleGrantedAuthority::new)
                .toList();

        // claims에서 인증 객체를 만들기 위한 customerId도 가져옴
        log.debug("customer info - customerId : {}", claims.getSubject());
        UserDetails userDetails = new User(claims.getSubject(), "", grantedAuthorities);

        //인증 객체 생성후 리턴
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, "", grantedAuthorities);
        log.info("Success make Authentication object");

        return authentication;
    }

    public long getAccessTokenExpiredTime(String accessToken) {
        Claims claims = null;
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(accessToken);
            claims = claimsJws.getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }

        return claims.getExpiration().getTime();
    }

    public String getAccessTokenSubject(String accessToken) {
        log.info("Get customerId by access tokens claims subject");
        Claims claims = null;
        try {
            // 토큰 파싱
            log.info("Parse access token claims");
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(accessToken);

            log.info("not expired token");
            claims = claimsJws.getBody();
        } catch (ExpiredJwtException e) {
            // 토큰이 만료 되었어도 customerId를 빼올 수 있어야 함
            log.info("expired token");
            claims = e.getClaims();
        }

        log.info("Success get customerId");
        return claims.getSubject();
    }

    public String genUniqueAccessToken(
            String customerId,
            Collection<? extends GrantedAuthority> authorities,
            ValueOperations<String, String> valueOperations
    ) {
        String accessToken = genAccessToken(customerId, authorities);
        while (valueOperations.get(accessToken) != null){
            accessToken = genAccessToken(customerId, authorities);
        }
        return accessToken;
    }
}
