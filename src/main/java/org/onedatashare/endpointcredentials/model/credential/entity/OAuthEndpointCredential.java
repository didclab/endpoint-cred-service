package org.onedatashare.endpointcredentials.model.credential.entity;

import lombok.Data;
import org.onedatashare.endpointcredentials.model.credential.encrypted.OAuthEndpointCredentialEncrypted;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * POJO for storing OAuth Credentials
 */
@Data
@Document
public class OAuthEndpointCredential extends EndpointCredential {
    private String token;
    private boolean tokenExpires = false;
    private Date expiresAt;
    private String refreshToken;
    private boolean refreshTokenExpires = false;

    public OAuthEndpointCredential(OAuthEndpointCredentialEncrypted credentialEncrypted) {
        this.accountId = credentialEncrypted.getAccountId();
        this.tokenExpires = credentialEncrypted.isTokenExpires();
        this.expiresAt = credentialEncrypted.getExpiresAt();
        this.refreshTokenExpires = credentialEncrypted.isRefreshTokenExpires();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}