package org.onedatashare.endpointcredentials.service;

import org.onedatashare.endpointcredentials.EndpointCredentialsApplication;
import org.onedatashare.endpointcredentials.model.credential.UserCredential;
import org.onedatashare.endpointcredentials.repository.CredListResponse;
import org.onedatashare.endpointcredentials.repository.UserCredentialRepository;
import org.onedatashare.endpointcredentials.model.credential.EndpointCredential;
import org.onedatashare.endpointcredentials.model.credential.EndpointCredentialType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Service that handles all operations to the endpoint-credential database
 */
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

    /**
     * Fetches credentialList of given type from the given userCredential object
     * @param userCredential
     * @param type
     * @return
     */
    private Set<String> getCredentialList(UserCredential userCredential, EndpointCredentialType type){
        try{
            Class classRef = Class.forName(UserCredential.class.getName());
            Method method = classRef.getDeclaredMethod("get" + StringUtils.capitalize(type.toString()));
            Object instance = userCredential;
            HashMap hashMap = (HashMap) method.invoke(instance);
            if(hashMap != null)
                return hashMap.keySet();
        } catch (ClassNotFoundException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException e) {
            EndpointCredentialsApplication.logger.error(e.getMessage());
        }
        return new HashSet<>();
    }

    /**
     * Fetches credential of given type from the given userCredential object
     * @param userCredential
     * @param type
     * @param id
     * @return
     */
    private EndpointCredential getCredential(UserCredential userCredential, EndpointCredentialType type, String id){
        try{
            Class classRef = Class.forName(UserCredential.class.getName());
            Method method = classRef.getDeclaredMethod("get" + StringUtils.capitalize(type.toString()));
            Object instance = userCredential;
            HashMap hashMap = (HashMap) method.invoke(instance);
            if(hashMap != null && hashMap.get(id)!=null)
                return (EndpointCredential) hashMap.get(id);
        } catch (ClassNotFoundException | IllegalAccessException
                | NoSuchMethodException | InvocationTargetException e) {
            EndpointCredentialsApplication.logger.error(e.getMessage());
        }
        return new EndpointCredential();
    }

    /**
     * Saves credential of given type in the database. It also handles concurrent updates
     * @param userId
     * @param type
     * @param endpointCredential
     * @return
     */
    /* Repository is not used to handle concurrent updates */
    public Mono<Void> saveCredential(String userId, EndpointCredentialType type, EndpointCredential endpointCredential) {
        Query query = getFindDocumentByIdQuery(userId);
        String accountId = encodeEmail(endpointCredential.getAccountId());
        Update update = new Update().set(createPath(type, accountId), endpointCredential);
        return template.upsert(query, update, UserCredential.class).then();
    }

    /**
     * Returns the list of credentials in given endpoint type
     * @param userId
     * @param type
     * @return
     */
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

    /**
     * Returns the credentials of given type and id
     * @param userId
     * @param type
     * @param accountId
     * @return
     */
    public Mono<EndpointCredential> fetchCredential(final String userId, final EndpointCredentialType type, final String accountId){
        String tempUserId = encodeEmail(userId), tempAccountId = encodeEmail(accountId);
        return repository.findById(tempUserId)
                .map(userCredential -> getCredential(userCredential, type, tempAccountId));
    }

    /**
     * Deletes a given credential from the database. Also handles concurrent updates
     * @param userId
     * @param type
     * @param accountId
     * @return
     */
    public Mono<Void> deleteCredential(final String userId, final EndpointCredentialType type, final String accountId){
        String tempAccountId = encodeEmail(accountId);
        Query query = getFindDocumentByIdQuery(userId);
        Update update = new Update().unset(createPath(type, accountId));
        return template.upsert(query, update, UserCredential.class).then();
    }
}
