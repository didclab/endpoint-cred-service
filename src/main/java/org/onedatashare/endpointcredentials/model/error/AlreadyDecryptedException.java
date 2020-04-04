package org.onedatashare.endpointcredentials.model.error;

public class AlreadyDecryptedException extends Exception{
    public AlreadyDecryptedException(){
        super("The secure field is already decrypted");
    }
}
