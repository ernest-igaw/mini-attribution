package com.dfinery.attribution.api.service

import com.dfinery.attribution.api.message.Sender
import com.dfinery.attribution.common.dto.EventDTO
import com.dfinery.attribution.common.entity.Profile
import com.dfinery.attribution.common.util.datetime.DateTimeUtil
import com.dfinery.attribution.common.util.uuid.UUIDGenerator.Companion.createRandomStringUUID
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class FirstOpenService (
    val sqsSender: Sender,
    @Value("\${variables.event.firstopen.sqs.name}")
    val firstOpenQueueName: String
) {
    companion object : KLogging()

    fun generateFirstOpenEvent(eventDTO: EventDTO): EventDTO {
//        val profileEntity = eventDTO.let {
//            val firstOpenLogId = it.logId ?: "${DateTimeUtil.getCurrentTimestamp()}:${createRandomStringUUID()}"
//            val trackerId = adtouchService.retrieveAdtouch(it.adKey)?.trackerId
//            Profile(it.adId, firstOpenLogId, it.adKey, trackerId)
//        }
//
//        profileRepository.save(profileEntity)
//        logger.info("Saved profile : $profileEntity")

        return sendSQS(eventDTO)
    }

    fun sendSQS(eventDTO: EventDTO): EventDTO {
        val sendResult = sqsSender.send(eventDTO.toString(), firstOpenQueueName)
        logger.info("Sent message to SQS : $sendResult")

        return eventDTO
    }
}
