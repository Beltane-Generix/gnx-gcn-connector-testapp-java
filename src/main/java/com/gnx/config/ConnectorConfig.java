package com.gnx.config;

import com.gnx.connector.Connector;
import com.gnx.connector.ConnectorFactory;
import com.gnx.connector.ConnectorInput;
import com.gnx.connector.exception.ConnectorException;
import com.gnx.kafka.KafkaAdapterInput;
import com.gnx.storage.StorageAdapterInput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectorConfig {

    @Value("${minio.url}")
    private String minioUrl;

    @Value("${minio.access.username}")
    private String minioUsername;

    @Value("${minio.access.password}")
    private String minioPassword;

    @Value("${minio.bucket.name}")
    private String minioBucket;

    @Value("${kafka.producer.bootstrap-servers}")
    private String bootstrapServer;

    @Value("${kafka.consumer.group-id}")
    private String kafkaGroupId;

    @Value("${application.hostname}")
    private String hostName;

    @Bean
    public Connector connector() throws ConnectorException {

        KafkaAdapterInput kafkaAdapterInput = new KafkaAdapterInput(bootstrapServer, kafkaGroupId);
        StorageAdapterInput storageAdapterInput = new StorageAdapterInput(minioUrl, minioUsername, minioPassword);

        ConnectorInput connectorInput = new ConnectorInput.Builder()
                .withKafkaAdapterInput(kafkaAdapterInput)
                .withStorageAdapterInput(storageAdapterInput)
                .withStorageBucket(minioBucket)
                .withHostName(hostName)
                .build();

        return ConnectorFactory.create(connectorInput);
    }
}
