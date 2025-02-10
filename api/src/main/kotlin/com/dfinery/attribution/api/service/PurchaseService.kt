package com.dfinery.attribution.api.service

import com.dfinery.attribution.api.message.Sender
import com.dfinery.attribution.common.dto.EventDTO
import com.dfinery.attribution.common.exception.InvalidEventItemException
import com.dfinery.attribution.common.util.datetime.DateTimeUtil
import com.dfinery.attribution.common.util.uuid.UUIDGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val sqsSender: Sender,
    @Value("\${variables.event.purchase.sqs.name}")
    private val purchaseQueueName: String
) {
    companion object : KLogging()

    fun generatePurchaseEvent(eventDTO: EventDTO): EventDTO {
        val createdTime = DateTimeUtil.getCurrentTimestamp().toString()
        val purchaseEventDTO = eventDTO.let {
            EventDTO(
                it.adId,
                it.eventType,
                it.logId ?: "${createdTime}:${UUIDGenerator.createRandomPartitionKey()}",
                it.adKey,
                it.items ?: throw InvalidEventItemException("The items are required for Purchase event. $it")
            )
        }
        return sendSQS(purchaseEventDTO)
    }

    private fun sendSQS(eventDTO: EventDTO): EventDTO {
        val sendResult = sqsSender.send(ObjectMapper().writeValueAsString(eventDTO), purchaseQueueName)
        logger.info("Sent SQS message : $sendResult")

        return eventDTO
    }
}
