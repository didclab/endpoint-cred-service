package org.onedatashare.endpointcredentials.model.credential;

import lombok.Data;
import org.onedatashare.endpointcredentials.model.error.AlreadyDecryptedException;
import org.onedatashare.endpointcredentials.model.error.AlreadyEncryptedException;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * POJO for storing account credential i.e., userName and Password
 */
@Data
@Document
public class AccountEndpointCredential extends EndpointCredential{
    protected String userName;
    private String password;
//    private transient boolean encrypted;

    public void encrypt(String key) throws AlreadyEncryptedException {
        if(this.encrypted){
            throw new AlreadyEncryptedException();
        }
        this.encrypted = true;
    }

    public void decrypt(String key) throws AlreadyDecryptedException {
        if(this.encrypted == false){
            throw new AlreadyDecryptedException();
        }
        this.encrypted = false;
    }
}
