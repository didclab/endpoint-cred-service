package org.onedatashare.endpointcredentials.encryption;

import com.mongodb.client.model.vault.EncryptOptions;
import org.bson.BsonBinary;
import org.bson.BsonString;
import org.onedatashare.endpointcredentials.model.credential.encrypted.OAuthEndpointCredentialEncrypted;
import org.onedatashare.endpointcredentials.model.credential.entity.EndpointCredential;
import org.onedatashare.endpointcredentials.model.credential.entity.OAuthEndpointCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OAuthEndpointCredentialHelper extends EndpointCredential {

    @Autowired
    protected KMSHandler kmsHandler;

    public static final String DETERMINISTIC_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic";
    public static final String RANDOM_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Random";


    public OAuthEndpointCredentialEncrypted getEncryptedOAuthEndpointCredential(OAuthEndpointCredential credential) {
        OAuthEndpointCredentialEncrypted credentialEncrypted = new OAuthEndpointCredentialEncrypted(credential);
        credentialEncrypted.setEncryptedToken(
                kmsHandler.getClientEncryption()
                        .encrypt(new BsonString(credential.getToken()), getEncryptOptions(RANDOM_ENCRYPTION_TYPE))
        );
        credentialEncrypted.setEncryptedRefreshToken(
                kmsHandler.getClientEncryption()
                        .encrypt(new BsonString(credential.getRefreshToken()), getEncryptOptions(RANDOM_ENCRYPTION_TYPE))
        );
        return credentialEncrypted;
    }

    public OAuthEndpointCredential getOAuthEndpointCredential(OAuthEndpointCredentialEncrypted credentialEncrypted){
        OAuthEndpointCredential credential = new OAuthEndpointCredential(credentialEncrypted);
        credential.setToken(kmsHandler.getClientEncryption().decrypt(credentialEncrypted.getEncryptedToken()).asString().getValue());
        credential.setRefreshToken(kmsHandler.getClientEncryption().decrypt(credentialEncrypted.getEncryptedRefreshToken()).asString().getValue());
        return credential;
    }

    private EncryptOptions getEncryptOptions(String algorithm){
        EncryptOptions encryptOptions = new EncryptOptions(algorithm);
        encryptOptions.keyId(new BsonBinary(kmsHandler.getEncryptionKeyUUID()));
        return encryptOptions;
    }
}
