package org.onedatashare.endpointcredentials.controller;

import org.onedatashare.endpointcredentials.model.credential.*;
import org.onedatashare.endpointcredentials.model.error.NoSuchCredentialException;
import org.onedatashare.endpointcredentials.repository.CredListResponse;
import org.onedatashare.endpointcredentials.service.UserCredentialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;


/**
 * Rest controller for handing request for operations on endpoint credential database
 */
@RestController
@RequestMapping("/endpoint-cred")
public class EndpointCredentialController {
    public static final Logger logger = LoggerFactory.getLogger(EndpointCredentialController.class);

    @Autowired
    private UserCredentialService userCredentialService;

    @GetMapping("/{type}")
    public Mono<CredListResponse> getCredential(@PathVariable EndpointCredentialType type, Mono<Principal> principal) {
        return principal.map(Principal::getName)
                .flatMap(userId -> userCredentialService.fetchCredentialList(userId, type));
    }

    @GetMapping("/{type}/{accountId}")
    public Mono<EndpointCredential> getCredential(@PathVariable EndpointCredentialType type,
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
    public Mono addCredential(@PathVariable OAuthCredentialType type,
                              @RequestBody OAuthEndpointCredential credential, Mono<Principal> principal) {
        return principal.map(Principal::getName)
                .flatMap(userId -> userCredentialService.saveCredential(userId, EndpointCredentialType.valueOf(type.toString()), credential));
    }

    @DeleteMapping("/{type}/{accountId}")
    public Mono<Void> deleteCredential(@PathVariable AccountCredentialType type,
                                       @PathVariable String accountId, Mono<Principal> principal) {
        return principal.map(Principal::getName)
                .flatMap(userId -> userCredentialService.deleteCredential(userId, EndpointCredentialType.valueOf(type.toString()), accountId));
    }

    @ExceptionHandler(NoSuchCredentialException.class)
    public ResponseEntity handle(NoSuchCredentialException exception){
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}