package com.onedatashare.endpointcredentials.model.request;

import com.onedatashare.endpointcredentials.model.credential.EndpointCredentialType;
import com.onedatashare.endpointcredentials.model.credential.OAuthEndpointCredential;
import lombok.Data;

@Data
public class SaveNewOAuthCredRequest {
    private String email;
    private EndpointCredentialType type;
    private OAuthEndpointCredential credential;
}
