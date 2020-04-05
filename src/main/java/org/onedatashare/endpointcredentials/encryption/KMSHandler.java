package org.onedatashare.endpointcredentials.encryption;

import com.mongodb.ClientEncryptionSettings;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.vault.DataKeyOptions;
import com.mongodb.client.vault.ClientEncryption;
import com.mongodb.client.vault.ClientEncryptions;
import org.bson.BsonBinary;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class KMSHandler {
    Logger logger = LoggerFactory.getLogger(KMSHandler.class);

    @Value("${spring.data.mongodb.uri}")
    private String dbConnectionUri;
    @Value("${spring.data.mongodb.key.vault.database}")
    private String keyVaultDatabase;
    @Value("${spring.data.mongodb.key.vault.collection}")
    private String keyVaultCollection;
    @Value("${spring.data.mongodb.kms.provider}")
    private String kmsProvider;
    @Value("${spring.data.mongodb.key.vault.name}")
    private String keyName;
    @Value("${spring.data.mongodb.encryption.masterkey.path}")
    private String masterKeyPath;

    private String encryptionKeyBase64;
    private UUID encryptionKeyUUID;

    public String getEncryptionKeyBase64() {
        return encryptionKeyBase64;
    }

    public UUID getEncryptionKeyUUID() {
        return encryptionKeyUUID;
    }

    public void buildOrValidateVault() {
        if(doesEncryptionKeyExist()){
            return;
        }
        DataKeyOptions dataKeyOptions = new DataKeyOptions();
        dataKeyOptions.keyAltNames(Arrays.asList(keyName));
        BsonBinary dataKeyId = getClientEncryption().createDataKey(kmsProvider, dataKeyOptions);

        this.encryptionKeyUUID = dataKeyId.asUuid();
        logger.debug("DataKeyID [UUID]{}",dataKeyId.asUuid());

        String base64DataKeyId = Base64.getEncoder().encodeToString(dataKeyId.getData());
        this.encryptionKeyBase64 = base64DataKeyId;
        logger.debug("DataKeyID [base64]: {}",base64DataKeyId);
    }

    private boolean doesEncryptionKeyExist(){
        MongoClient mongoClient = MongoClients.create(dbConnectionUri);
        MongoCollection<Document> collection = mongoClient.getDatabase(keyVaultDatabase).getCollection(keyVaultCollection);

        Bson query = Filters.in("keyAltNames", keyName);
        Document doc = collection
                .find(query)
                .first();

        return Optional.ofNullable(doc)
                .map(o -> {
                    logger.debug("The Document is {}",doc);
                    this.encryptionKeyUUID = (UUID) o.get("_id");
                    this.encryptionKeyBase64 = Base64.getEncoder().encodeToString(new BsonBinary((UUID) o.get("_id")).getData());
                    return true;
                })
                .orElse(false);
    }


    private byte[] getMasterKey()  {

        byte[] localMasterKey= new byte[96];

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(masterKeyPath);
            fis.read(localMasterKey, 0, 96);
        } catch (Exception e) {
            logger.error("Error Initializing the master key");
        }
        return localMasterKey;
    }

    private Map<String, Map<String, Object>> getKMSMap() {
        Map<String, Object> keyMap = Stream.of(
                new AbstractMap.SimpleEntry<>("key",getMasterKey())
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return Stream.of(
                new AbstractMap.SimpleEntry<>(kmsProvider, keyMap)
        ).collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
    }

    public ClientEncryption getClientEncryption() {


        String keyVaultNamespace = keyVaultDatabase +"."+ keyVaultCollection;
        ClientEncryptionSettings clientEncryptionSettings = ClientEncryptionSettings.builder()
                .keyVaultMongoClientSettings(MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString(dbConnectionUri))
                        .build())
                .keyVaultNamespace(keyVaultNamespace)
                .kmsProviders(this.getKMSMap())
                .build();

        ClientEncryption clientEncryption = ClientEncryptions.create(clientEncryptionSettings);

        return clientEncryption;
    }
}
