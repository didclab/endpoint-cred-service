package org.onedatashare.endpointcredentials.model.request;

import org.onedatashare.endpointcredentials.model.credential.AccountEndpointCredential;
import org.onedatashare.endpointcredentials.model.credential.EndpointCredentialType;
import lombok.Data;

@Data
public class SaveNewAccountCredRequest {
    private EndpointCredentialType type;
    private AccountEndpointCredential credential;
}
