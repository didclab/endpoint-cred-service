package org.onedatashare.endpointcredentials.model.credential.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * POJO for storing OAuth Credentials
 */
@Data
@Document
public class OAuthEndpointCredential extends EndpointCredential {
    private String token;
    private byte[] encryptedToken;
    private boolean tokenExpires = false;
    private Date expiresAt;
    private String refreshToken;
    private byte[] encryptedRefreshToken;
    private boolean refreshTokenExpires = false;

    public void setEncryptedToken(byte[] encryptedToken){
        this.encryptedToken = encryptedToken;
        this.token = null;
    }

    public void setEncryptedRefreshToken(byte[] encryptedRefreshToken){
        this.encryptedRefreshToken = encryptedRefreshToken;
        this.token = null;
    }

    public void setToken(String token){
        this.token = token;
        this.encryptedToken = null;
    }

    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
        this.encryptedRefreshToken = null;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}