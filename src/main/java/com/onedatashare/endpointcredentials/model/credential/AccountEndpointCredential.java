package com.onedatashare.endpointcredentials.model.credential;

import lombok.Data;


/**
 * POJO for storing account credential i.e., userName and Password
 */
@Data
public class AccountEndpointCredential extends EndpointCredential{
    private String password;

    @Override
    public void encrypt(String key) {
    }

    @Override
    public void decrypt(String key) {
    }
}
