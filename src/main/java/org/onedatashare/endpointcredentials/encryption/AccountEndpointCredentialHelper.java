package org.onedatashare.endpointcredentials.encryption;

import com.mongodb.client.model.vault.EncryptOptions;
import org.bson.BsonBinary;
import org.bson.BsonString;
import org.onedatashare.endpointcredentials.model.credential.AccountEndpointCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountEndpointCredentialHelper {

    @Autowired
    protected KMSHandler kmsHandler;

    public static final String DETERMINISTIC_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic";
    public static final String RANDOM_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Random";


    public AccountEndpointCredential getEncryptedAccountEndpointCredential(AccountEndpointCredential credential) {
        AccountEndpointCredential credentialEncrypted = (AccountEndpointCredential) credential.clone();
        if(credential.getSecret() != null) {
            BsonBinary bsonBinary = kmsHandler.getClientEncryption()
                    .encrypt(new BsonString(credential.getSecret()), getEncryptOptions(RANDOM_ENCRYPTION_TYPE));
            credentialEncrypted.setEncryptedSecret(bsonBinary.getData());
        }
        credentialEncrypted.setSecret(null);
        return credentialEncrypted;
    }

    public AccountEndpointCredential getAccountEndpointCredential(AccountEndpointCredential credentialEncrypted){
        AccountEndpointCredential credential = (AccountEndpointCredential) credentialEncrypted.clone();
        if(credentialEncrypted.getEncryptedSecret() != null) {
            credential.setSecret(kmsHandler.getClientEncryption().decrypt(
                    new BsonBinary(credentialEncrypted.getEncryptedSecret()))
                    .asString().getValue());
        }
        credential.setEncryptedSecret(null);
        return credential;
    }

    private EncryptOptions getEncryptOptions(String algorithm){
        EncryptOptions encryptOptions = new EncryptOptions(algorithm);
        encryptOptions.keyId(new BsonBinary(kmsHandler.getEncryptionKeyUUID()));
        return encryptOptions;
    }

}