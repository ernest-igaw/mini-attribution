package com.dfinery.attribution.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.awspring.cloud.dynamodb.DynamoDbTemplate
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

@Configuration
class DynamoDbConfig {
    @Value("\${spring.cloud.aws.region.static}")
    val region: String? = null

    private fun credentialsProvider() = DefaultCredentialsProvider.builder().build()

    @Bean
    fun dynamoDbClient(): DynamoDbClient {
        return DynamoDbClient.builder()
            .credentialsProvider(credentialsProvider())
            .region(Region.of(region))
            .build()
    }

    @Bean
    fun dynamoDbEnhancedClient(
        @Qualifier("dynamoDbClient") dynamoDbClient: DynamoDbClient
    ): DynamoDbEnhancedClient {
        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build()
    }

    @Bean
    fun dynamoDbTemplate(dynamoDbEnhancedClient: DynamoDbEnhancedClient): DynamoDbTemplate {
        return DynamoDbTemplate(dynamoDbEnhancedClient)
    }
}