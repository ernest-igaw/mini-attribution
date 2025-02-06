package com.dfinery.attribution.api.message

import com.dfinery.attribution.api.config.SqsConfig
import io.awspring.cloud.sqs.operations.SendResult
import org.springframework.stereotype.Component

@Component
class Sender(private val sqsConfig: SqsConfig) {
    fun send(payload: String, queueName: String): SendResult<String> = sqsConfig.sqsTemplate()
        .send { sendOpsTo -> sendOpsTo.queue(queueName)
            .payload(payload)
        }
}