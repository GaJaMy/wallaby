package com.wallaby.moamoa.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// SpringDoc OpenAPI Config
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {

        // 기본 문서 설정
        Info info = new Info().version("0.2.3")
                .title("DepRx API 명세서")
                .description(
                        "### DepRx API의 Swagger 명세서입니다.\n" +
                                "모든 API의 Content-Type은 `application/json`입니다.\n\n" +
                                "**JWT가 포함되는 API의 경우, 다음과 같은 헤더가 포함되어야 합니다:**\n" +
                                "- `key: Content-Type`, `value: application/json`\n" +
                                "- `key: Authorization`, `value: Bearer accessToken`\n\n" +
                                "추가 정보는 아래 내용을 참고하세요."
                );

        // jwt를 사용하기 위한 문서 설정
        // 헤더에 토큰을 포함시켜 준다.
        String jwtSchemaName = "bearerAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemaName);

        Components components = new Components().addSecuritySchemes(jwtSchemaName, new SecurityScheme()
                .name(jwtSchemaName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
        );

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components)
                ;
    }
}
