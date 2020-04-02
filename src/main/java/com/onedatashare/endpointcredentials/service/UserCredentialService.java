package com.onedatashare.endpointcredentials.service;

import com.onedatashare.endpointcredentials.model.credential.*;
import com.onedatashare.endpointcredentials.model.error.AlreadyDecryptedException;
import com.onedatashare.endpointcredentials.model.error.AlreadyEncryptedException;
import com.onedatashare.endpointcredentials.repository.CredListResponse;
import com.onedatashare.endpointcredentials.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class UserCredentialService {
    @Value("crypt.key")
    private String key;

    @Autowired
    ReactiveMongoTemplate template;

    @Autowired
    UserCredentialRepository repository;

    /* Repository is not used to handle concurrency */
    public Mono<Void> saveCredential(String userName, EndpointCredentialType type, EndpointCredential endpointCredential) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(userName.replace(".", "!")));
        String accountId = endpointCredential.getAccountId().replace(".", "!");
        Update update = new Update().set("credentialMap."+ type + "." + accountId, endpointCredential);
        return template.upsert(query, update, UserCredential.class).then();
    }

    public Mono<CredListResponse> fetchCredentialList(final String userName, final String type){
        return repository.findById(userName).map(userCredential -> {
            Set<String> availableCredAccountList = new HashSet<>();
            Map<String, EndpointCredential> endpointCredentialMap = userCredential.getCredentialMap().get(type);
            if(endpointCredentialMap != null){
                availableCredAccountList = endpointCredentialMap.keySet();
            }
            return new CredListResponse(availableCredAccountList);
        });
    }

    public Mono<EndpointCredential> getCredential(final String userName, final String type, final String accountId){
        String tempAccountId = accountId.replace(".","!");
        return repository.findById(userName).map(userCredential -> {
            EndpointCredential endpointCredential = new EndpointCredential();
            Map<String, EndpointCredential> endpointCredentialMap = userCredential.getCredentialMap().get(type);
            if(endpointCredentialMap != null){
                endpointCredential = endpointCredentialMap.get(tempAccountId);
            }
            return endpointCredential;
        });

    }

    public Mono<Void> deleteCredential(final String userName, final String type, final String accountId){
        String tempAccountId = accountId.replace(".","!");
        Query query = new Query().addCriteria(Criteria.where("_id").is(userName.replace(".", "!")));
        Update update = new Update().unset("credentialMap."+ type + "." + tempAccountId);
        return template.upsert(query, update, UserCredential.class).then();
    }
}
