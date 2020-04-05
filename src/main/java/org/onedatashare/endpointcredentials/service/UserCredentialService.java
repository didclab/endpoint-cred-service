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

import java.util.*;

@Service
public class UserCredentialService {
    @Autowired
    ReactiveMongoTemplate template;

    @Autowired
    UserCredentialRepository repository;

    private static Query getFindDocumentByIdQuery(String userId){
        return new Query().addCriteria(Criteria.where("_id").is(encodeEmail(userId)));
    }

    private static String encodeEmail(String email){
        return email.replace(".","!");
    }

    private static String decodeEmail(String email){
        return email.replace("!", ".");
    }

    private static String createPath(EndpointCredentialType type, String accoundId){
        return type.toString() + '.' + accoundId;
    }

    private Set<String> getCredentialList(UserCredential userCredential, EndpointCredentialType type){
        switch (type){
            case dropbox: return userCredential.getDropbox().keySet();
            case box: return userCredential.getBox().keySet();
            case ftp: return userCredential.getFtp().keySet();
            case http: return userCredential.getHttp().keySet();
            case sftp:;return userCredential.getBox().keySet();
            case gdrive: return userCredential.getGdrive().keySet();
            case globus: return userCredential.getGlobus().keySet();
            case s3: return userCredential.getS3().keySet();
            default:
                return new HashSet<>();
        }
    }

    private EndpointCredential getCredential(UserCredential userCredential, EndpointCredentialType type, String id){
        EndpointCredential endpointCredential = null;
        if(type == EndpointCredentialType.dropbox && userCredential.getDropbox() != null)
            endpointCredential = userCredential.getDropbox().get(id);
        else if(type == EndpointCredentialType.gdrive && userCredential.getGdrive() != null)
            endpointCredential = userCredential.getGdrive().get(id);
        else if(type == EndpointCredentialType.box && userCredential.getBox() != null)
            endpointCredential = userCredential.getBox().get(id);
        else if(type == EndpointCredentialType.globus && userCredential.getGlobus() != null)
            endpointCredential = userCredential.getGlobus().get(id);
        else if(type == EndpointCredentialType.sftp && userCredential.getSftp() != null)
            endpointCredential = userCredential.getSftp().get(id);
        else if(type == EndpointCredentialType.ftp && userCredential.getFtp() != null)
            endpointCredential = userCredential.getFtp().get(id);
        else if(type == EndpointCredentialType.http && userCredential.getHttp() != null)
            endpointCredential = userCredential.getHttp().get(id);
        else if(type == EndpointCredentialType.s3 && userCredential.getS3() != null)
            endpointCredential = userCredential.getS3().get(id);

        if(endpointCredential == null){
            endpointCredential = new EndpointCredential();
        }
        return endpointCredential;
    }

    /* Repository is not used to handle concurrent updates */
    public Mono<Void> saveCredential(String userId, EndpointCredentialType type, EndpointCredential endpointCredential) {
        Query query = getFindDocumentByIdQuery(userId);
        String accountId = encodeEmail(endpointCredential.getAccountId());
        Update update = new Update().set(createPath(type, accountId), endpointCredential);
        return template.upsert(query, update, UserCredential.class).then();
    }

    public Mono<CredListResponse> fetchCredentialList(final String userId, final EndpointCredentialType type){
        return repository.findById(encodeEmail(userId)).map(userCredential -> {
            List<String> availableCredAccountList = new ArrayList<>();
            Set<String> endpointCredentialSet = getCredentialList(userCredential, type);
            if(endpointCredentialSet != null){
                for(String accountId : endpointCredentialSet){
                    availableCredAccountList.add(decodeEmail(accountId));
                }
            }
            return new CredListResponse(availableCredAccountList);
        });
    }

    public Mono<EndpointCredential> fetchCredential(final String userId, final EndpointCredentialType type, final String accountId){
        String tempUserId = encodeEmail(userId), tempAccountId = encodeEmail(accountId);
        return repository.findById(tempUserId)
                .map(userCredential -> getCredential(userCredential, type, tempAccountId));
    }

    /* Repository is not used to handle concurrency */
    public Mono<Void> deleteCredential(final String userId, final EndpointCredentialType type, final String accountId){
        String tempAccountId = encodeEmail(accountId);
        Query query = getFindDocumentByIdQuery(userId);
        Update update = new Update().unset(createPath(type, accountId));
        return template.upsert(query, update, UserCredential.class).then();
    }
}
