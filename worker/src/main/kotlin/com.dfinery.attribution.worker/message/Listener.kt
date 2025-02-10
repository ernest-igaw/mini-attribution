package com.dfinery.attribution.worker.message

import com.dfinery.attribution.worker.service.FirstOpenWorker
import com.dfinery.attribution.worker.service.PurchaseWorker
import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.sqs.annotation.SqsListener
import io.awspring.cloud.sqs.listener.acknowledgement.BatchAcknowledgement
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component

@Component
class Listener(
    val firstOpenWorker: FirstOpenWorker,
    val purchaseWorker: PurchaseWorker
) {
    @SqsListener(value = ["\${variables.event.firstopen.sqs.name}"], factory = "sqsListenerContainerFactory")
    fun listenFirstOpen(
        payloads: List<String>,
        batchAcknowledgement: BatchAcknowledgement<String>
    ) {
        runBlocking {
            payloads.asFlow()
                .flatMapMerge(concurrency = 10) { payload ->
                    flow {
                        val firstopenMsg = ObjectMapper().readTree(payload)
                        // 병렬 처리 가능한 작업 수행 (예: suspend 함수 호출)
                        firstOpenWorker.parseFirstOpen(firstopenMsg)
                        emit(Unit)
                    }
                }
                .collect {}
        }
        // 병렬 처리 작업이 완료되면 SQS 메시지를 삭제
        batchAcknowledgement.acknowledge()
    }

    @SqsListener(value = ["\${variables.event.purchase.sqs.name}"], factory = "sqsListenerContainerFactory")
    fun listenPurchase(
        payloads: List<String>,
        batchAcknowledgement: BatchAcknowledgement<String>
    ) {
        runBlocking {
            payloads.asFlow()
                .flatMapMerge(concurrency = 10) { payload ->
                    flow {
                        val purchaseMsg = ObjectMapper().readTree(payload)
                        purchaseWorker.parsePurchase(purchaseMsg)
                        emit(Unit)
                    }
                }
                .collect {}
        }
        // 병렬 처리 작업이 완료되면 SQS 메시지를 삭제
        batchAcknowledgement.acknowledge()
    }
}