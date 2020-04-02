package com.onedatashare.endpointcredentials.model.request;

import com.onedatashare.endpointcredentials.model.credential.AccountEndpointCredential;
import com.onedatashare.endpointcredentials.model.credential.EndpointCredentialType;
import lombok.Data;

@Data
public class SaveNewAccountCredRequest {
    private String email;
    private EndpointCredentialType type;
    private AccountEndpointCredential credential;
}
