package com.awsS3.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class SecretManagerConfig {

//    @Value(value = "${database.name}")
//    String awsAccessKey;

    @Bean(value = "AwsSecret")
    public String getSecret() {
//        log.info("AWS access key is :"+awsAccessKey);

        String accessKey = "AKIAZ24ISRUKO4BPTVUF";
//                System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = "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";
//                System.getenv("AWS_SECRET_ACCESS_KEY");

        AwsCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        SecretsManagerClient secretsManagerClient = SecretsManagerClient.builder()
                .region(Region.US_EAST_1) // Set your region
                .credentialsProvider(() -> awsCredentials)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId("s3_credential")
                .build();


        GetSecretValueResponse response = secretsManagerClient.getSecretValue(getSecretValueRequest);
        log.info("Value is :" + response.secretString());
        return null;

    }


}