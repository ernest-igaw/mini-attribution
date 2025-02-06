package com.dfinery.attribution.worker.message

import io.awspring.cloud.sqs.annotation.SqsListener
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.stereotype.Component

@Component
class Listener {
    @SqsListener(value = ["dfn-mini-attribution-event-firstopen-dev", "dfn-mini-attribution-event-purchase-dev"], factory = "sqsListenerContainerFactory")
    fun listen(payload: Any, @Headers headers: MessageHeaders, acknowledgement: Acknowledgement) {
        println("Received message: $payload")
        acknowledgement.acknowledge()
    }
}