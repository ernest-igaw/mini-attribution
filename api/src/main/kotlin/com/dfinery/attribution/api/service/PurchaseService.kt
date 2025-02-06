package com.dfinery.attribution.api.service

import com.dfinery.attribution.api.message.Sender
import com.dfinery.attribution.common.dto.EventDTO
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
//        val profileEntity = profileRepository.findByAdId(eventDTO.adId) ?: Profile(
//            eventDTO.adId,
//            "",
//            eventDTO.adKey,
//            adtouchService.retrieveAdtouch(eventDTO.adKey)?.trackerId
//        )
//
//        logger.info("Retrieved Profile : $profileEntity")

        return sendSQS(eventDTO)
    }

    fun sendSQS(eventDTO: EventDTO): EventDTO {
        val sendResult = sqsSender.send(eventDTO.toString(), purchaseQueueName)
        logger.info("Sent SQS message : $sendResult")

        return eventDTO
    }
}
