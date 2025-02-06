package com.dfinery.attribution.worker.config

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory
import io.awspring.cloud.sqs.listener.SqsMessageListenerContainer
import io.awspring.cloud.sqs.listener.acknowledgement.AcknowledgementOrdering
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode
import io.awspring.cloud.sqs.operations.SqsTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import java.time.Duration

@Configuration
class SqsConfig {
    @Value("\${spring.cloud.aws.region.static}")
    val region: String? = null

    private fun credentialsProvider() = DefaultCredentialsProvider.builder().build()

    @Bean
    fun sqsAsyncClient(): SqsAsyncClient = SqsAsyncClient.builder()
        .credentialsProvider(credentialsProvider())
        .region(Region.of(region))
        .build()

    @Bean
    fun sqsListenerContainerFactory(): SqsMessageListenerContainerFactory<Any> =
        SqsMessageListenerContainerFactory.builder<Any>()
            .configure { opt ->
                opt.acknowledgementMode(AcknowledgementMode.MANUAL)
//                    .acknowledgementInterval(Duration.ofSeconds(3L))
//                    .acknowledgementThreshold(5)
//                    .acknowledgementOrdering(AcknowledgementOrdering.ORDERED)
            }
            .sqsAsyncClient(sqsAsyncClient())
            .build()

    @Bean
    fun sqsTemplate(): SqsTemplate = SqsTemplate.newTemplate(sqsAsyncClient())
}