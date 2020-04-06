package org.onedatashare.endpointcredentials.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import org.onedatashare.endpointcredentials.encryption.KMSHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
public class ReactiveMongoConfig extends AbstractReactiveMongoConfiguration {
    @Value("${spring.data.mongodb.name}")
    private String databaseName;

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    @Autowired
    private KMSHandler kmsHandler;
    @Override
    public MongoClient reactiveMongoClient() {
        kmsHandler.buildOrValidateVault();
        return MongoClients.create(connectionString);
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(reactiveMongoClient(), databaseName);
    }
}
