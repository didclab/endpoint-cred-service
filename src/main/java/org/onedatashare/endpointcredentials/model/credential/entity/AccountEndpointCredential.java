package org.onedatashare.endpointcredentials.model.credential.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.onedatashare.endpointcredentials.model.credential.encrypted.AccountEndpointCredentialEncrypted;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * POJO for storing account credential i.e., userName and Password
 */
@Data
@Document
@NoArgsConstructor
public class AccountEndpointCredential extends EndpointCredential {
    private String secret;

    public AccountEndpointCredential(AccountEndpointCredentialEncrypted credentialEncrypted) {
        this.accountId = credentialEncrypted.getAccountId();
    }
}
