package com.dfinery.attribution.api.service

import com.dfinery.attribution.api.message.Sender
import com.dfinery.attribution.common.dto.EventDTO
import com.dfinery.attribution.common.exception.InvalidEventLogIdException
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    val sqsSender: Sender,
    @Value("\${variables.event.purchase.sqs.name}")
    val purchaseQueueName: String
) {
    companion object : KLogging()

    fun generatePurchaseEvent(eventDTO: EventDTO): EventDTO {
        if (eventDTO.logId == null) {
            throw InvalidEventLogIdException("The logId is required for Purchase event. $eventDTO")
        }
        return sendSQS(eventDTO)
    }

    fun sendSQS(eventDTO: EventDTO): EventDTO {
        val sendResult = sqsSender.send(ObjectMapper().writeValueAsString(eventDTO), purchaseQueueName)
        logger.info("Sent SQS message : $sendResult")

        return eventDTO
    }
}
