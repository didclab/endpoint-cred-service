package org.onedatashare.endpointcredentials.model.credential;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.HashMap;

/**
 * This class is used for storing all the credential for a given user
 */
@Data
public class UserCredential {
    @Id
    private String email;
    private HashMap<String, OAuthEndpointCredential> dropbox;
    private HashMap<String, OAuthEndpointCredential> gdrive;
    private HashMap<String, OAuthEndpointCredential> box;
    private HashMap<String, OAuthEndpointCredential> gftp;
    private HashMap<String, AccountEndpointCredential> http;
    private HashMap<String, AccountEndpointCredential> ftp;
    private HashMap<String, AccountEndpointCredential> sftp;
    private HashMap<String, AccountEndpointCredential> s3;
}