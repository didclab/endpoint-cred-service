package org.onedatashare.endpointcredentials.model.error;

import org.onedatashare.endpointcredentials.model.credential.EndpointCredentialType;

public class NoSuchCredentialException extends Exception{
    public NoSuchCredentialException(EndpointCredentialType type, String credId){
        super("No such credential found :" + type + "/" + credId);
    }
}
