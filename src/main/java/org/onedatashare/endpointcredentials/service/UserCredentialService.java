package org.onedatashare.endpointcredentials.service;

import org.onedatashare.endpointcredentials.encryption.AccountEndpointCredentialHelper;
import org.onedatashare.endpointcredentials.encryption.OAuthEndpointCredentialHelper;
import org.onedatashare.endpointcredentials.model.credential.AccountEndpointCredential;
import org.onedatashare.endpointcredentials.model.credential.OAuthEndpointCredential;
import org.onedatashare.endpointcredentials.model.credential.UserCredential;
import org.onedatashare.endpointcredentials.model.error.NoSuchCredentialException;
import org.onedatashare.endpointcredentials.repository.CredListResponse;
import org.onedatashare.endpointcredentials.repository.UserCredentialRepository;
import org.onedatashare.endpointcredentials.model.credential.EndpointCredential;
import org.onedatashare.endpointcredentials.model.credential.EndpointCredentialType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private ReactiveMongoTemplate template;

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private AccountEndpointCredentialHelper accountEndpointCredentialHelper;

    @Autowired
    private OAuthEndpointCredentialHelper oAuthEndpointCredentialHelper;

    private static final Logger logger = LoggerFactory.getLogger(UserCredentialService.class);

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
            Class classRef = UserCredential.class;
            Method method = classRef.getDeclaredMethod("get" + StringUtils.capitalize(type.toString()));
            HashMap hashMap = (HashMap) method.invoke(userCredential);
            if(hashMap != null)
                return hashMap.keySet();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            logger.error(e.getMessage());
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
            Class classRef = UserCredential.class;
            Method method = classRef.getDeclaredMethod("get" + StringUtils.capitalize(type.toString()));
            HashMap hashMap = (HashMap) method.invoke(userCredential);
            if(hashMap != null && hashMap.get(id) != null)
                return (EndpointCredential) hashMap.get(id);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            logger.error(e.getMessage());
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
        if(endpointCredential instanceof AccountEndpointCredential) {
            endpointCredential = accountEndpointCredentialHelper.getEncryptedAccountEndpointCredential((AccountEndpointCredential) endpointCredential);
        }else if(endpointCredential instanceof OAuthEndpointCredential){
            endpointCredential = oAuthEndpointCredentialHelper.getEncryptedOAuthEndpointCredential((OAuthEndpointCredential) endpointCredential);
        }
        Update update = new Update().set(createPath(type, accountId), endpointCredential);
        return template.upsert(query, update, UserCredential.class)
                .then();
    }

    /**
     * Returns the list of credentials in given endpoint type
     * @param userId
     * @param type
     * @return
     */
    public Mono<CredListResponse> fetchCredentialList(final String userId, final EndpointCredentialType type){
        return userCredentialRepository.findById(encodeEmail(userId))
                .map(userCredential -> {
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
        return userCredentialRepository.findById(tempUserId)
                .map(userCredential -> getCredential(userCredential, type, tempAccountId))
                .filter(endpointCredential -> endpointCredential.getAccountId() != null)
                .switchIfEmpty(Mono.error(new NoSuchCredentialException(type, accountId)))
                .map((credential) -> {
                    if(credential instanceof AccountEndpointCredential) {
                        credential = accountEndpointCredentialHelper.getAccountEndpointCredential((AccountEndpointCredential) credential);
                    }else if(credential instanceof OAuthEndpointCredential){
                        credential = oAuthEndpointCredentialHelper.getOAuthEndpointCredential((OAuthEndpointCredential) credential);
                    }
                    return credential;
                });
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
        Update update = new Update().unset(createPath(type, tempAccountId));
        return template.upsert(query, update, UserCredential.class)
                .then();
    }
}