package com.onedatashare.endpointcredentials.model.credential;

import com.onedatashare.endpointcredentials.model.error.AlreadyDecryptedException;
import com.onedatashare.endpointcredentials.model.error.AlreadyEncryptedException;
import com.onedatashare.endpointcredentials.model.error.InvalidTypeException;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


/**
 * Base class for storing one user credential
 */
@Data
@Document
public class EndpointCredential extends Object{
    protected String accountId;
    protected EndpointCredentialType type;
    protected boolean encrypted = false;

    public void encrypt(String key) throws AlreadyEncryptedException {
        throw new NotImplementedException();
    }

    public void decrypt(String key) throws AlreadyDecryptedException {
        throw new NotImplementedException();
    }

    @SneakyThrows
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof EndpointCredential)) {
            throw new InvalidTypeException(EndpointCredential.class.toString());
        }
        return accountId.toLowerCase().equals(((EndpointCredential) obj).accountId.toLowerCase());
    }
}