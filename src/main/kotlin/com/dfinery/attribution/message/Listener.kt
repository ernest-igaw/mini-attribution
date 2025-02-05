package com.dfinery.attribution.message

import io.awspring.cloud.sqs.annotation.SqsListener
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.stereotype.Component

@Component
class Listener {
    @SqsListener(value = ["\${variables.event.firstopen.sqs.name}", "\${variables.event.purchase.sqs.name}"])
    fun listen(payload: Any, @Headers headers: MessageHeaders, acknowledgement: Acknowledgement) {
        println("Received message: $payload")
        acknowledgement.acknowledge()
    }
}