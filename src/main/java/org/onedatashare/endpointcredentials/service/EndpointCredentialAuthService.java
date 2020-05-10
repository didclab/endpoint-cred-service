//package org.onedatashare.endpointcredentials.service;
//
//import org.onedatashare.endpointcredentials.component.JWTUtil;
//import org.onedatashare.endpointcredentials.model.core.Role;
//import io.jsonwebtoken.Claims;
//import org.onedatashare.endpointcredentials.EndpointCredentialsApplication;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.ReactiveAuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class EndpointCredentialAuthService implements ReactiveAuthenticationManager {
//    @Autowired
//    private JWTUtil jwtUtil;
//
//    private static final Logger logger = LoggerFactory.getLogger(EndpointCredentialAuthService.class);
//    @Override
//    public Mono<Authentication> authenticate(Authentication authentication) {
//        String authToken = authentication.getCredentials().toString();
//
//        String userName;
//        try{
//            userName = jwtUtil.getEmailFromToken(authToken);
//        } catch (Exception e){
//            logger.error("Invalid token");
//            return Mono.empty();
//        }
//
//        if(userName != null && jwtUtil.validateToken(authToken)){
//            Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
//            List<String> roleList = claims.get("role", List.class);
//            List<Role> roles = new ArrayList<>();
//            if(roleList != null) {
//                for (String role : roleList) {
//                    roles.add(Role.valueOf(role));
//                }
//            }
//            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                    userName,
//                    authToken,
//                    roles.stream().map(authority -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList())
//            );
//            return Mono.just(auth);
//        }
//        return Mono.empty();
//    }
//}