//package org.onedatashare.endpointcredentials.config;
//
//import org.onedatashare.endpointcredentials.repository.EndpointCredSecurityRepository;
//import org.onedatashare.endpointcredentials.service.EndpointCredentialAuthService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity
//public class SpringSecurityConfig {
//    @Autowired
//    private EndpointCredentialAuthService authService;
//
//    @Autowired
//    private EndpointCredSecurityRepository securityRepository;
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        return http
//                .httpBasic().disable()
//                .authenticationManager(authService)
//                .securityContextRepository(securityRepository)
//                .authorizeExchange()
//                //Permit all the HTTP methods
//                .pathMatchers(HttpMethod.OPTIONS).permitAll()
//                //Need authentication for APICalls
//                .pathMatchers("/**").authenticated()
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint(this::authenticationFailedHandler).accessDeniedHandler(this::accessDeniedHandler)
//                .and()
//                .formLogin().disable()
//                .csrf().disable().authorizeExchange().and()
//                .build();
//    }
//
//    private Mono<Void> authenticationFailedHandler(ServerWebExchange serverWebExchange, AuthenticationException e) {
//        return Mono.fromRunnable(() -> {
//            serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//        });
//    }
//
//    private Mono<Void> accessDeniedHandler(ServerWebExchange serverWebExchange, AccessDeniedException e) {
//        return Mono.fromRunnable(() -> {
//            serverWebExchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//        });
//    }
//}
