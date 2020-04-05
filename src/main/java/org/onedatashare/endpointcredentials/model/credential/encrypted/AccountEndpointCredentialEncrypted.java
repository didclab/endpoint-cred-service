package org.onedatashare.endpointcredentials.model.credential.encrypted;

import lombok.Data;
import org.bson.BsonBinary;
import org.onedatashare.endpointcredentials.model.credential.entity.EndpointCredential;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class AccountEndpointCredentialEncrypted extends EndpointCredential {
    private BsonBinary encryptedSecret;
}
