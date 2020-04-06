package org.onedatashare.endpointcredentials.model.credential.entity;

import lombok.Data;
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

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}