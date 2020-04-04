package org.onedatashare.endpointcredentials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EndpointCredentialsApplication {
    public static final Logger logger = LoggerFactory.getLogger(EndpointCredentialsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EndpointCredentialsApplication.class, args);
    }

}
