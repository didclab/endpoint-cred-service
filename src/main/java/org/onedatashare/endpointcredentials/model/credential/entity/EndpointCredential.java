package org.onedatashare.endpointcredentials.model.credential.entity;

import lombok.Data;
import lombok.SneakyThrows;
import org.onedatashare.endpointcredentials.model.error.InvalidTypeException;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Base class for storing one user credential
 */
@Data
@Document
public class EndpointCredential extends Object implements Cloneable{
    protected String accountId;

    @SneakyThrows
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof EndpointCredential)) {
            throw new InvalidTypeException(EndpointCredential.class.toString());
        }
        return accountId.toLowerCase().equals(((EndpointCredential) obj).accountId.toLowerCase());
    }

    @Override
    public Object clone(){
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}