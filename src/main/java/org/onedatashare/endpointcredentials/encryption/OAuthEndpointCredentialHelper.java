package org.onedatashare.endpointcredentials.encryption;

import com.mongodb.client.model.vault.EncryptOptions;
import org.bson.BsonBinary;
import org.bson.BsonString;
import org.onedatashare.endpointcredentials.model.credential.EndpointCredential;
import org.onedatashare.endpointcredentials.model.credential.OAuthEndpointCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OAuthEndpointCredentialHelper extends EndpointCredential {

    @Autowired
    protected KMSHandler kmsHandler;

    public static final String DETERMINISTIC_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic";
    public static final String RANDOM_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Random";


    public OAuthEndpointCredential getEncryptedOAuthEndpointCredential(OAuthEndpointCredential credential) {
        OAuthEndpointCredential credentialEncrypted = (OAuthEndpointCredential) credential.clone();
        if(credential.getToken() != null) {
            credentialEncrypted.setEncryptedToken(
                    kmsHandler.getClientEncryption()
                            .encrypt(new BsonString(credential.getToken()), getEncryptOptions(RANDOM_ENCRYPTION_TYPE)).getData()
            );
        }
        if(credential.getRefreshToken() != null) {
            credentialEncrypted.setEncryptedRefreshToken(
                    kmsHandler.getClientEncryption()
                            .encrypt(new BsonString(credential.getRefreshToken()), getEncryptOptions(RANDOM_ENCRYPTION_TYPE))
                    .getData()
            );
        }
        credentialEncrypted.setToken(null);
        credentialEncrypted.setRefreshToken(null);
        return credentialEncrypted;
    }

    public OAuthEndpointCredential getOAuthEndpointCredential(OAuthEndpointCredential credentialEncrypted){
        OAuthEndpointCredential credential = (OAuthEndpointCredential) credentialEncrypted.clone();
        if(credentialEncrypted.getEncryptedToken() != null) {
            credential.setToken(kmsHandler.getClientEncryption().decrypt(
                    new BsonBinary(credentialEncrypted.getEncryptedToken())).asString().getValue());
        }
        if(credentialEncrypted.getEncryptedRefreshToken() != null) {
            credential.setRefreshToken(kmsHandler.getClientEncryption()
                    .decrypt(new BsonBinary(credentialEncrypted.getEncryptedRefreshToken())).asString().getValue());
        }
        credential.setEncryptedToken(null);
        credential.setEncryptedRefreshToken(null);
        return credential;
    }

    private EncryptOptions getEncryptOptions(String algorithm){
        EncryptOptions encryptOptions = new EncryptOptions(algorithm);
        encryptOptions.keyId(new BsonBinary(kmsHandler.getEncryptionKeyUUID()));
        return encryptOptions;
    }
}
