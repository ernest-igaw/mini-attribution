package com.dfinery.attribution.worker.message

import com.dfinery.attribution.worker.service.FirstOpenWorker
import com.dfinery.attribution.worker.service.PurchaseWorker
import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.annotation.SqsListener
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.stereotype.Component

@Component
class Listener(
    val firstOpenWorker: FirstOpenWorker,
    val purchaseWorker: PurchaseWorker
) {
    @SqsListener(value = ["dfn-mini-attribution-event-firstopen-dev"], factory = "sqsListenerContainerFactory")
    fun listenFirstOpen(payload: String, @Headers headers: MessageHeaders, acknowledgement: Acknowledgement) {
        val firstopenMsg = ObjectMapper().readTree(payload)
        acknowledgement.acknowledge()
        firstOpenWorker.parseFirstOpen(firstopenMsg)
    }

    @SqsListener(value = ["dfn-mini-attribution-event-purchase-dev"], factory = "sqsListenerContainerFactory")
    fun listenPurchase(payload: String, @Headers headers: MessageHeaders, acknowledgement: Acknowledgement) {
        val purchaseMsg = ObjectMapper().readTree(payload)
        acknowledgement.acknowledge()
        purchaseWorker.parsePurchase(purchaseMsg)
    }
}