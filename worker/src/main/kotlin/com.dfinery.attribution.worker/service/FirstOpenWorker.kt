package com.dfinery.attribution.worker.service

import com.dfinery.attribution.common.dto.AdtouchDTO
import com.dfinery.attribution.common.dto.EventDTO
import com.dfinery.attribution.common.entity.Profile
import com.dfinery.attribution.common.exception.AdtouchNotFoundException
import mu.KLogging
import com.dfinery.attribution.worker.repository.ProfileRepository
import com.dfinery.attribution.common.util.datetime.DateTimeUtil
import com.dfinery.attribution.common.util.uuid.UUIDGenerator
import com.dfinery.attribution.worker.repository.AdtouchRepository

class FirstOpenWorker (
    val adtouchRepository: AdtouchRepository,
    val profileRepository: ProfileRepository,
) {
    companion object : KLogging()

    fun retrieveAdtouch(adKey: String): AdtouchDTO? {
        return adtouchRepository.findByAdKey(adKey)?.let {
            AdtouchDTO(it.adKey, it.trackerId, it.createdAt)
        } ?: throw AdtouchNotFoundException("No Adtouch found for the passed in adKey : $adKey")
    }

    fun generateFirstOpenEvent(eventDTO: EventDTO): EventDTO {
        val profileEntity = eventDTO.let {
            val firstOpenLogId = it.logId ?: "${DateTimeUtil.getCurrentTimestamp()}:${UUIDGenerator.createRandomStringUUID()}"
            val trackerId = retrieveAdtouch(it.adKey)?.trackerId
            Profile(it.adId, firstOpenLogId, it.adKey, trackerId)
        }

        profileRepository.save(profileEntity)
        logger.info("Saved Profile : $profileEntity")

        return sendSQS(eventDTO)
    }

    fun newProfile(profile: Profile): Profile {
        return profile
    }

    fun sendSQS(eventDTO: EventDTO): EventDTO {
        return eventDTO
    }
}