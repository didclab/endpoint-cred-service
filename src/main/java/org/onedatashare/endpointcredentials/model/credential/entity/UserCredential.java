package org.onedatashare.endpointcredentials.model.credential.entity;

import lombok.Data;
import org.onedatashare.endpointcredentials.model.credential.encrypted.AccountEndpointCredentialEncrypted;
import org.onedatashare.endpointcredentials.model.credential.encrypted.OAuthEndpointCredentialEncrypted;

import java.util.HashMap;

/**
 * This class is used for storing all the credential for a given user
 */
@Data
public class UserCredential {
    private String email;
    private HashMap<String, OAuthEndpointCredentialEncrypted> dropbox;
    private HashMap<String, OAuthEndpointCredentialEncrypted> gdrive;
    private HashMap<String, OAuthEndpointCredentialEncrypted> box;
    private HashMap<String, OAuthEndpointCredentialEncrypted> globus;
    private HashMap<String, AccountEndpointCredentialEncrypted> http;
    private HashMap<String, AccountEndpointCredentialEncrypted> ftp;
    private HashMap<String, AccountEndpointCredentialEncrypted> sftp;
    private HashMap<String, AccountEndpointCredentialEncrypted> s3;
}