package org.onedatashare.endpointcredentials.model.credential.encrypted;

import lombok.Data;
import org.bson.BsonBinary;
import org.onedatashare.endpointcredentials.model.credential.entity.EndpointCredential;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
public class OAuthEndpointCredentialEncrypted extends EndpointCredential {
    private BsonBinary encryptedToken;
    private boolean tokenExpires = false;
    private Date expiresAt;
    private String encryptedRefreshToken;
    private boolean refreshTokenExpires = false;

}
