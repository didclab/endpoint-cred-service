package org.onedatashare.endpointcredentials.repository;

import org.onedatashare.endpointcredentials.model.credential.entity.UserCredential;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserCredentialRepository extends ReactiveMongoRepository<UserCredential, String> {
}