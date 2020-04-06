package org.onedatashare.endpointcredentials.model.credential.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * POJO for storing account credential i.e., userName and Password
 */
@Data
@Document
public class AccountEndpointCredential extends EndpointCredential {
    private String secret;
    private byte[] encryptedSecret;

    public void setEncryptedSecret(byte[] encryptedSecret){
        this.encryptedSecret = encryptedSecret;
        this.secret = null;
    }

    public void setSecret(String s){
        this.secret = s;
        this.encryptedSecret = null;
    }
}
