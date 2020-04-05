package org.onedatashare.endpointcredentials.model.credential;

import lombok.Data;

import java.util.HashMap;

/**
 * This class is used for storing all the credential for a given user
 */
@Data
public class UserCredential {
    private String email;
    private HashMap<String, HashMap<String, AccountEndpointCredential>> credentialMap;
}