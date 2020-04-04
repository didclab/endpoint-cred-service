//package com.onedatashare.endpointcredentials.config;
//
//import com.onedatashare.endpointcredentials.model.credential.EndpointCredential;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.ReactiveKeyCommands;
//import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
//import org.springframework.data.redis.connection.ReactiveStringCommands;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.ReactiveRedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import javax.annotation.PreDestroy;
//
//
//
//@Configuration
//public class RedisConfig {
//
//    @Autowired
//    RedisConnectionFactory factory;
//
//    @Value("${reddis.host}")
//    private String host;
//
//    @Value("${reddis.port}")
//    private int port;
//
//    @Bean
//    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
//        return new LettuceConnectionFactory(host, port);
//    }
//
//    @Bean
//    public ReactiveRedisTemplate<String, EndpointCredential> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
//        Jackson2JsonRedisSerializer<EndpointCredential> serializer = new Jackson2JsonRedisSerializer<>(EndpointCredential.class);
//        RedisSerializationContext.RedisSerializationContextBuilder<String, EndpointCredential> builder = RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
//        RedisSerializationContext<String, EndpointCredential> context = builder.value(serializer)
//                .build();
//        return new ReactiveRedisTemplate<>(factory, context);
//    }
//
//    @Bean
//    public ReactiveRedisTemplate<String, String> reactiveRedisTemplateString(ReactiveRedisConnectionFactory connectionFactory) {
//        return new ReactiveRedisTemplate<>(connectionFactory, RedisSerializationContext.string());
//    }
//
//    @Bean
//    public ReactiveKeyCommands keyCommands(final ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
//        return reactiveRedisConnectionFactory.getReactiveConnection()
//                .keyCommands();
//    }
//
//    @Bean
//    public ReactiveStringCommands stringCommands(final ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
//        return reactiveRedisConnectionFactory.getReactiveConnection()
//                .stringCommands();
//    }
//
//    @PreDestroy
//    public void cleanRedis() {
//        factory.getConnection()
//                .flushDb();
//    }
//
//}
