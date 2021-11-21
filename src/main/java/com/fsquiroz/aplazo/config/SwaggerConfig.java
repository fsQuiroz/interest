package com.fsquiroz.aplazo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
@Profile("!test")
@Slf4j
public class SwaggerConfig {

    private Docket docket;

    @Bean
    public Docket api() {
        if (docket == null) {
            docket = new Docket(DocumentationType.OAS_30)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("com.fsquiroz.aplazo"))
                    .build()
                    .apiInfo(
                            new ApiInfo(
                                    "Interest",
                                    "Simple Interest Service",
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    Collections.EMPTY_LIST
                            )
                    );
        }
        return docket;
    }
}
