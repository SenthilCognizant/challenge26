package com.gds.challenge26.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPI (Swagger) documentation.
 * <p>
 * Defines metadata used to generate interactive API documentation
 * for the application's REST endpoints.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Creates and configures the {@link OpenAPI} definition for the application.
     * <p>
     * The configuration includes:
     * <ul>
     *   <li>API title</li>
     *   <li>API version</li>
     *   <li>API description</li>
     * </ul>
     *
     * @return a configured {@link OpenAPI} instance
     */
    @Bean
    public OpenAPI challangeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GDC Challenge API")
                        .version("1.0.0")
                       .description("Swagger documentation for user & Meeting APIs"));
    }
}
