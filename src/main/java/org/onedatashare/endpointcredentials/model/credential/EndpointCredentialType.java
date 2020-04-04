package org.onedatashare.endpointcredentials.model.credential;

public enum EndpointCredentialType {
    BOX("box"),
    DROPBOX("dropbox"),
    GDRIVE("gdrive"),
    GLOBUS("globus"),
    HTTP("http"),
    FTP("ftp"),
    S3("s3"),
    SFTP("sftp");

    private final String text;

    EndpointCredentialType(final String text){
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}