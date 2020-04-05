package org.onedatashare.endpointcredentials.encryption;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

public class KeyGenerator {
    public static void main(String[] args){
        byte[] localMasterKey = new byte[96];
        new SecureRandom().nextBytes(localMasterKey);
        try (FileOutputStream stream = new FileOutputStream("master-key.txt")) {
            stream.write(localMasterKey);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
