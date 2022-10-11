package com.eden.hackerdesktopapi.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Config of DynamoDB
 *
 * @author Mo Xu
 * @date 2022/08/31
 */
@Configuration
@EnableDynamoDBRepositories(basePackages = "com.eden.hackerdesktopapi.repository")
public class DynamoDBConfig {
    @Value("${aws.dynamodb.accessKey}")
    private String accessKey;

    @Value("${aws.dynamodb.secretKey}")
    private String secretKey;

    @Value("${aws.dynamodb.region}")
    private String region;

    public AWSCredentialsProvider amazonAWSCredentialsProvider() {
        return new AWSStaticCredentialsProvider(amazonAWSCredentials());
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        return DynamoDBMapperConfig.DEFAULT;
    }

    //@Bean
    //public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
    //    return new DynamoDBMapper(amazonDynamoDB, config);
    //}

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        //return AmazonDynamoDBClientBuilder.standard().withCredentials(amazonAWSCredentialsProvider())
        //        .withRegion(region).build();
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(region).build();
    }
}
