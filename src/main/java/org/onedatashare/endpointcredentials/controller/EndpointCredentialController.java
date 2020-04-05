package org.onedatashare.endpointcredentials.controller;

import org.onedatashare.endpointcredentials.model.credential.AccountEndpointCredential;
import org.onedatashare.endpointcredentials.model.credential.EndpointCredential;
import org.onedatashare.endpointcredentials.model.credential.EndpointCredentialType;
import org.onedatashare.endpointcredentials.model.credential.OAuthEndpointCredential;
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
    public Mono<CredListResponse> getCredential(@PathVariable String type, Mono<Principal> principal) {
        return principal.map(Principal::getName)
                .flatMap(userId -> userCredentialService.fetchCredentialList(userId, type));
    }

    @GetMapping("/{type}/{accountId}")
    public Mono<EndpointCredential> getCredential(@PathVariable String type,
                                                  @PathVariable String accountId, Mono<Principal> principal) {
        return principal.map(Principal::getName)
                .flatMap(userId -> userCredentialService.fetchCredential(userId, type, accountId));
    }

    @PostMapping("/account-cred/{type}")
    public Mono<Void> addCredential(@PathVariable EndpointCredentialType type,
                                    @RequestBody AccountEndpointCredential credential, Mono<Principal> principal) {
        return principal.map(Principal::getName)
                .flatMap(userId -> userCredentialService.saveCredential(userId, type, credential));
    }

    @PostMapping("/oauth-cred/{type}")
    public Mono addCredential(@PathVariable EndpointCredentialType type,
                              @RequestBody OAuthEndpointCredential credential, Mono<Principal> principal) {
        return principal.map(Principal::getName)
                .flatMap(userId -> userCredentialService.saveCredential(userId, type, credential));
    }

    @DeleteMapping("/{type}/{accountId}")
    public Mono<Void> deleteCredential(@PathVariable String type,
                                       @PathVariable String accountId, Mono<Principal> principal) {
        return principal.map(Principal::getName)
                .flatMap(userId -> userCredentialService.deleteCredential(userId, type, accountId));
    }
}