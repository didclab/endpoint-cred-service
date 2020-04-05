package org.onedatashare.endpointcredentials.controller;

import org.onedatashare.endpointcredentials.model.credential.EndpointCredential;
import org.onedatashare.endpointcredentials.model.request.SaveNewAccountCredRequest;
import org.onedatashare.endpointcredentials.model.request.SaveNewOAuthCredRequest;
import org.onedatashare.endpointcredentials.repository.CredListResponse;
import org.onedatashare.endpointcredentials.service.UserCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/endpoint-cred")
public class EndpointCredentialController {
    @Autowired
    private UserCredentialService userCredentialService;

    @GetMapping("/{type}")
    public Mono<CredListResponse> getCredential(@PathVariable String type, Mono<Principal> principal){
        return principal.map(Principal::getName)
                .flatMap(userId -> userCredentialService.fetchCredentialList(userId ,type));
    }

    @GetMapping("/{type}/{id}")
    public Mono<EndpointCredential> getCredential(@PathVariable String type, @PathVariable String accountId, Mono<Principal> principal){
        return principal.map(Principal::getName)
                .flatMap(userId -> userCredentialService.getCredential(userId ,type, accountId));
    }

    @PostMapping("/account-cred")
    public Mono<Void> addCredential(@RequestBody SaveNewAccountCredRequest request, Mono<Principal> principal){
       return principal.map(Principal::getName)
               .flatMap(userId -> userCredentialService.saveCredential(userId, request.getType(), request.getCredential()));
    }

    @PostMapping("/oauth-cred")
    public Mono addCredential(@RequestBody SaveNewOAuthCredRequest request, Mono<Principal> principal){
        return principal.map(Principal::getName)
                .flatMap(userId -> userCredentialService.saveCredential(userId, request.getType(), request.getCredential()));
    }

    @DeleteMapping("/{type}/{id}")
    public Mono<Void> deleteCredential(@PathVariable String type, @PathVariable String id, Mono<Principal> principal){
        return principal.map(Principal::getName).flatMap(userId -> userCredentialService.deleteCredential(userId, type, id));
    }
}