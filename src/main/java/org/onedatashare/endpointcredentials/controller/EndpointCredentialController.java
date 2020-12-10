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
@RequestMapping("v1/endpoint-cred")
public class EndpointCredentialController {
    public static final Logger logger = LoggerFactory.getLogger(EndpointCredentialController.class);

    @Autowired
    private UserCredentialService userCredentialService;

    @GetMapping("/{userId}/{type}")
    public Mono<CredListResponse> getCredential(@PathVariable String userId,
                                                @PathVariable EndpointCredentialType type) {
        return userCredentialService.fetchCredentialList(userId, type);
    }

    @GetMapping("/{userId}/{type}/{accountId}")
    public Mono<EndpointCredential> getCredential(@PathVariable EndpointCredentialType type,
                                                  @PathVariable String accountId, @PathVariable String userId) {
        return userCredentialService.fetchCredential(userId, type, accountId);
    }

    @PostMapping("/{userId}/account-cred/{type}")
    public Mono<Void> addCredential(@PathVariable EndpointCredentialType type,
                                    @RequestBody AccountEndpointCredential credential, @PathVariable String userId) {
        return userCredentialService.saveCredential(userId, type, credential);
    }

    @PostMapping("/{userId}/oauth-cred/{type}")
    public Mono addCredential(@PathVariable OAuthCredentialType type,
                              @RequestBody OAuthEndpointCredential credential, @PathVariable String userId) {
        return userCredentialService.saveCredential(userId, EndpointCredentialType.valueOf(type.toString()), credential);
    }

    @DeleteMapping("/{userId}/{type}/{accountId}")
    public Mono<Void> deleteCredential(@PathVariable EndpointCredentialType type,
                                       @PathVariable String accountId, @PathVariable String userId) {
        return userCredentialService.deleteCredential(userId, EndpointCredentialType.valueOf(type.toString()), accountId);
    }

    @ExceptionHandler(NoSuchCredentialException.class)
    public ResponseEntity handle(NoSuchCredentialException exception){
        logger.error(exception.getMessage());
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}