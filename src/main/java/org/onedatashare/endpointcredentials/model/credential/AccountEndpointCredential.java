package org.onedatashare.endpointcredentials.model.credential;

import lombok.Data;
import org.onedatashare.endpointcredentials.model.error.AlreadyDecryptedException;
import org.onedatashare.endpointcredentials.model.error.AlreadyEncryptedException;


/**
 * POJO for storing account credential i.e., userName and Password
 */
@Data
public class AccountEndpointCredential extends EndpointCredential{
    private String password;

    @Override
    public void encrypt(String key) throws AlreadyEncryptedException {
        if(this.encrypted){
            throw new AlreadyEncryptedException();
        }
        this.encrypted = true;
    }

    @Override
    public void decrypt(String key) throws AlreadyDecryptedException {
        if(this.encrypted == false){
            throw new AlreadyDecryptedException();
        }
        this.encrypted = false;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
