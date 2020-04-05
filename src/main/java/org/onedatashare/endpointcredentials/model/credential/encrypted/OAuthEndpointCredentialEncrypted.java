package org.onedatashare.endpointcredentials.model.credential.encrypted;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.BsonBinary;
import org.onedatashare.endpointcredentials.model.credential.entity.EndpointCredential;
import org.onedatashare.endpointcredentials.model.credential.entity.OAuthEndpointCredential;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@NoArgsConstructor
public class OAuthEndpointCredentialEncrypted extends EndpointCredential {
    private BsonBinary encryptedToken;
    private boolean tokenExpires = false;
    private Date expiresAt;
    private BsonBinary encryptedRefreshToken;
    private boolean refreshTokenExpires = false;

    public OAuthEndpointCredentialEncrypted(OAuthEndpointCredential credential) {
        this.accountId = credential.getAccountId();
        this.tokenExpires = credential.isTokenExpires();
        this.expiresAt = credential.getExpiresAt();
        this.refreshTokenExpires = credential.isRefreshTokenExpires();
    }
}
