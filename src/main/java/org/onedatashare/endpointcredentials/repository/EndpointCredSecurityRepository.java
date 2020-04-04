package org.onedatashare.endpointcredentials.repository;

import org.onedatashare.endpointcredentials.component.JWTUtil;
import org.onedatashare.endpointcredentials.service.EndpointCredentialAuthService;
import io.jsonwebtoken.ExpiredJwtException;
import org.onedatashare.endpointcredentials.EndpointCredentialsApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Service
public class EndpointCredSecurityRepository  implements ServerSecurityContextRepository {
    private final String TOKEN_PREFIX = "Bearer ";
    private final int TOKEN_LEN = TOKEN_PREFIX.length();

    @Autowired
    private EndpointCredentialAuthService authService;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
        return null;
    }

    public String fetchAuthToken(ServerWebExchange serverWebExchange){
        ServerHttpRequest request = serverWebExchange.getRequest();
        String token = null;
        //Check for token only when the request needs to be authenticated
        try {
            token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if(token.length() < TOKEN_LEN) {
                token = null;
            }
            token = token.substring(TOKEN_LEN);
        } catch (NullPointerException npe) {
            EndpointCredentialsApplication.logger.error("Token Expired");
        }
        return token;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        String authToken = this.fetchAuthToken(serverWebExchange);
        try {
            if (authToken != null) {
                String email = jwtUtil.getEmailFromToken(authToken);
                Authentication auth = new UsernamePasswordAuthenticationToken(email, authToken);
                return this.authService.authenticate(auth).map(SecurityContextImpl::new);
            }
        }
        catch(ExpiredJwtException e){
            //TODO: add error log
//            ODSLoggerService.logError("Token Expired");
        }
        return Mono.empty();
    }
}
