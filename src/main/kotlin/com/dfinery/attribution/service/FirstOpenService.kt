package com.dfinery.attribution.service

import com.dfinery.attribution.dto.EventDTO
import com.dfinery.attribution.entity.Profile
import com.dfinery.attribution.repository.ProfileRepository
import com.dfinery.attribution.util.datetime.DateTimeUtil
import com.dfinery.attribution.util.uuid.UUIDGenerator.Companion.createRandomStringUUID
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class FirstOpenService (
    val profileRepository: ProfileRepository,
    val adtouchService: AdtouchService
) {
    companion object : KLogging()

    fun generateFirstOpenEvent(eventDTO: EventDTO): EventDTO {
        val profileEntity = eventDTO.let {
            val firstOpenId = it.logId ?: "${DateTimeUtil.getCurrentTimestamp()}:${createRandomStringUUID()}"
            val trackerId = adtouchService.retrieveAdtouch(it.adKey)?.trackerId

            Profile(it.adId, firstOpenId, it.adKey, trackerId)
        }

        profileRepository.save(profileEntity)
        logger.info("Saved Profile : $profileEntity")

        return eventDTO
//        return eventDTO.let {
//            EventDTO(it.adId, it.eventType, eventEntity.firstOpenLogId, it.adKey, it.items)
//        }
    }

    fun newProfile(profile: Profile): Profile {
        return profile
    }

    fun sendSQS(eventDTO: EventDTO): EventDTO {
        return eventDTO
    }
}
