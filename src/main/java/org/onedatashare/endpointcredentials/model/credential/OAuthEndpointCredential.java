package org.onedatashare.endpointcredentials.model.credential;

import org.onedatashare.endpointcredentials.model.error.AlreadyDecryptedException;
import org.onedatashare.endpointcredentials.model.error.AlreadyEncryptedException;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * POJO for storing OAuth Credentials
 */
@Data
@Document
public class OAuthEndpointCredential extends EndpointCredential{
    private String token;
    private boolean tokenExpires = false;
    private Date expiresAt;
    private String refreshToken;
    private boolean refreshTokenExpires = false;

    public OAuthEndpointCredential(EndpointCredentialType type, String token) {
        this.type = type;
        this.token = token;
    }

    @Override
    public void encrypt(String key) throws AlreadyEncryptedException{
        if(this.encrypted){
            throw new AlreadyEncryptedException();
        }
        this.encrypted = true;
    }

    @Override
    public void decrypt(String key) throws AlreadyDecryptedException {
        if(this.encrypted == false){
            throw new AlreadyDecryptedException();
        }
        this.encrypted = false;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

}