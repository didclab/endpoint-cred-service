package org.onedatashare.endpointcredentials.model.request;

import org.onedatashare.endpointcredentials.model.credential.EndpointCredentialType;
import org.onedatashare.endpointcredentials.model.credential.OAuthEndpointCredential;
import lombok.Data;

@Data
public class SaveNewOAuthCredRequest {
    private EndpointCredentialType type;
    private OAuthEndpointCredential credential;
}
