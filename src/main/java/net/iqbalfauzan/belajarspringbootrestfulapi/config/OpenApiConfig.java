package net.iqbalfauzan.belajarspringbootrestfulapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * Created by IqbalMF on 2024.
 * Package net.iqbalfauzan.belajarspringbootrestfulapi.config
 */
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Iqbal M Fauzan",
                        email = "iqbalmf68@gmail.com",
                        url = "iqbalfauzan.net"
                ),
                description = "Documentation of Belajar Spring Boot Restful Api",
                title = "Api Docs",
                version = "1.0.0"
        )
)
public class OpenApiConfig {
}
