package com.onedatashare.endpointcredentials.model.credential;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used for storing all the credential for a given user
 */
@Data
@Document
public class UserCredential {
    @Id
    private String email;
    private HashMap<String, HashMap<String, EndpointCredential>> credentialMap;
}