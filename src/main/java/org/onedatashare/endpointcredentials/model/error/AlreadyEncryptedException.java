package org.onedatashare.endpointcredentials.model.error;

public class AlreadyEncryptedException extends Exception{
    public AlreadyEncryptedException(){
        super("The field is already encrypted");
    }
}
