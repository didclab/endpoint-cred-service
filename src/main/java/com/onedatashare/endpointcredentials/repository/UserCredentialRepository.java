package com.onedatashare.endpointcredentials.repository;

import com.onedatashare.endpointcredentials.model.credential.UserCredential;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserCredentialRepository extends ReactiveMongoRepository<UserCredential, String> {
}