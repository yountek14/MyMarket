package com.mymarket.ms_ventas.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI configurarOpenApi() {
        Contact contacto = new Contact()
                .name("MyMarket Team")
                .email("contacto@mymarket.cl")
                .url("https://www.mymarket.cl");

        License licencia = new License()
                .name("MIT")
                .url("https://opensource.org/licenses/MIT");

        Info informacionApi = new Info()
                .description("""
                        API para la gestion de ventas
                        del sistema MyMarket
                        """)
                .version("1.0")
                .contact(contacto)
                .license(licencia);

        ExternalDocumentation github = new ExternalDocumentation()
                .description("Repositorio oficial del proyecto en GitHub")
                .url("https://github.com/MyMarket");

        return new OpenAPI()
                .info(informacionApi)
                .externalDocs(github);
    }
}
