package org.onedatashare.endpointcredentials.service;

import org.onedatashare.endpointcredentials.model.credential.UserCredential;
import org.onedatashare.endpointcredentials.repository.CredListResponse;
import org.onedatashare.endpointcredentials.repository.UserCredentialRepository;
import org.onedatashare.endpointcredentials.model.credential.EndpointCredential;
import org.onedatashare.endpointcredentials.model.credential.EndpointCredentialType;
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

    private static Query getFindDocumentByIdQuery(String userName){
        return new Query().addCriteria(Criteria.where("_id").is(userName.replace(".", "!")));
    }

    private static String formatString(String email){
        return email.replace(".","!");
    }

    private static String unformatString(String email){
        return email.replace("!", ".");
    }

    /* Repository is not used to handle concurrency */
    public Mono<Void> saveCredential(String userName, EndpointCredentialType type, EndpointCredential endpointCredential) {
        Query query = getFindDocumentByIdQuery(userName);
        String accountId = formatString(endpointCredential.getAccountId());
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
        String tempAccountId = formatString(accountId);
        return repository.findById(userName).map(userCredential -> {
            EndpointCredential endpointCredential = new EndpointCredential();
            Map<String, EndpointCredential> endpointCredentialMap = userCredential.getCredentialMap().get(type);
            if(endpointCredentialMap != null){
                endpointCredential = endpointCredentialMap.get(tempAccountId);
            }
            return endpointCredential;
        });

    }

    /* Repository is not used to handle concurrency */
    public Mono<Void> deleteCredential(final String userName, final String type, final String accountId){
        String tempAccountId = formatString(accountId);
        Query query = new Query().addCriteria(Criteria.where("_id").is(userName.replace(".", "!")));
        Update update = new Update().unset("credentialMap."+ type + "." + tempAccountId);
        return template.upsert(query, update, UserCredential.class).then();
    }
}
