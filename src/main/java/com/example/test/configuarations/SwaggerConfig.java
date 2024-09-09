package com.example.test.configuarations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;

@Configuration
public class SwaggerConfig {

    @Value("${open.api.title}")
    private String title;

    @Value("${open.api.version}")
    private String version;

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description("This is a sample API")
                        .license(new License().name("Apache 2.0").url("http://www.apache.org//licenses/LICECSE-2.0.html")));
    }

    @Bean
    public GroupedOpenApi publicApi1() {
        return GroupedOpenApi.builder()
                .group("admin-api")
                .pathsToMatch("/api/v1/admin/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicApi2() {
        return GroupedOpenApi.builder()
                .group("layout-api")
                .pathsToMatch("/api/v1/layout/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicApi3() {
        return GroupedOpenApi.builder()
                .group("student-api")
                .pathsToMatch("/api/v1/student/**")
                .build();
    }
}