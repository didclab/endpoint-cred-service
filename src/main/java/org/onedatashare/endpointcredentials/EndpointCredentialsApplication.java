package org.onedatashare.endpointcredentials;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(
        info = @Info(
                title = "OneDataShare REST API",
                version = "1.0",
                description = "OpenAPI REST API documentation",
                license = @License(name = "Apache-2.0", url = "https://github.com/didclab/onedatashare/blob/master/LICENSE"),
                contact = @Contact(url = "http://onedatashare.org", name = "OneDataShare Team", email = "admin@onedatashare.org")
        ),
        servers = {
                @Server(
                        description = "ODS backend",
                        url = "http://onedatashare.org"
                )
        }
)

public class EndpointCredentialsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EndpointCredentialsApplication.class, args);
    }

}
