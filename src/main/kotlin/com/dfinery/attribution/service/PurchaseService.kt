package com.dfinery.attribution.service

import com.dfinery.attribution.dto.EventDTO
import com.dfinery.attribution.entity.Profile
import com.dfinery.attribution.repository.ProfileRepository
import com.dfinery.attribution.util.datetime.DateTimeUtil
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    val profileRepository: ProfileRepository,
    val adtouchService: AdtouchService
) {
    companion object : KLogging()

    fun generatePurchaseEvent(eventDTO: EventDTO): EventDTO {
        val eventEntity = eventDTO.let {
            val firstOpenId = it.logId!!
            val trackerId = adtouchService.retrieveAdtouch(it.adKey)?.trackerId

            Profile(it.adId, firstOpenId, it.adKey, trackerId)
        }

        profileRepository.save(eventEntity)
        logger.info("Saved Event : $eventEntity")

        return eventDTO.let {
            EventDTO(it.adId, it.eventType, eventEntity.firstOpenLogId, it.adKey, it.items)
        }
    }

    fun sendSQS(eventDTO: EventDTO): EventDTO {
        val createdTime = DateTimeUtil.getCurrentTimestamp().toString()

        return eventDTO
//        return eventEntity.let {
//            EventDTO(it.adKey, it.trackerId, it.createdAt)
//        }
    }
}
