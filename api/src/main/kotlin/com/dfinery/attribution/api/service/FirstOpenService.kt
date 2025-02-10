package com.dfinery.attribution.api.service

import com.dfinery.attribution.api.message.Sender
import com.dfinery.attribution.common.dto.EventDTO
import com.dfinery.attribution.common.exception.AdtouchNotFoundException
import com.dfinery.attribution.common.util.datetime.DateTimeUtil
import com.dfinery.attribution.common.util.uuid.UUIDGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class FirstOpenService(
    private val sqsSender: Sender,
    @Value("\${variables.event.firstopen.sqs.name}")
    private val firstOpenQueueName: String
) {
    companion object : KLogging()

    fun generateFirstOpenEvent(eventDTO: EventDTO): EventDTO {
        val createdTime = DateTimeUtil.getCurrentTimestamp().toString()
        val firstOpenDTO = eventDTO.let {
            EventDTO(
                it.adId,
                it.eventType,
                it.logId ?: "${createdTime}:${UUIDGenerator.createRandomPartitionKey()}",
                it.adKey ?: throw AdtouchNotFoundException("The adKey is required for FirstOpen event. $it")
            )
        }
        return sendSQS(firstOpenDTO)
    }

    private fun sendSQS(eventDTO: EventDTO): EventDTO {
        val sendResult = sqsSender.send(ObjectMapper().writeValueAsString(eventDTO), firstOpenQueueName)
        logger.info("Sent message to SQS : $sendResult")

        return eventDTO
    }
}
