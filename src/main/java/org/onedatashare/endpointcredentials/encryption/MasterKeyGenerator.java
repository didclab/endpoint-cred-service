package org.onedatashare.endpointcredentials.encryption;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MasterKeyGenerator {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        byte[] localMasterKey;
        String masterKeyPhrase = System.getenv("ODS_ENPOINT_CRED_MASTER_KEY_SECRET");
        if(masterKeyPhrase == null){
            throw new IOException("No key secret found");
        }
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        localMasterKey = messageDigest.digest(masterKeyPhrase.getBytes());
        localMasterKey = Arrays.copyOf(localMasterKey, 96);
        try (FileOutputStream stream = new FileOutputStream("masterKey.txt")) {
            stream.write(localMasterKey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Master Key Generation Successful");
    }
}
