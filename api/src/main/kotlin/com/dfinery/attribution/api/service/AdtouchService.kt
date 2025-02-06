package com.dfinery.attribution.api.service

import com.dfinery.attribution.common.dto.AdtouchDTO
import com.dfinery.attribution.common.entity.Adtouch
import com.dfinery.attribution.common.exception.AdtouchNotFoundException
import com.dfinery.attribution.api.repository.AdtouchRepository
import com.dfinery.attribution.common.util.datetime.DateTimeUtil
import org.springframework.stereotype.Service
import mu.KLogging

@Service
class AdtouchService(
    val adtouchRepository: AdtouchRepository
) {
    companion object : KLogging()

    fun generateAdtouch(adtouchDTO: AdtouchDTO): AdtouchDTO {
        val createdTime = DateTimeUtil.getCurrentTimestamp().toString()

        val adtouchEntity = if (adtouchDTO.adKey != null) {
            Adtouch(adKey = adtouchDTO.adKey!!, trackerId = adtouchDTO.trackerId, createdAt = createdTime)
        } else {
            Adtouch(trackerId = adtouchDTO.trackerId, createdAt = createdTime)
        }

        logger.info("Saved Adtouch : $adtouchEntity")
        adtouchRepository.save(adtouchEntity)

        return adtouchEntity.let {
            AdtouchDTO(it.adKey, it.trackerId, it.createdAt)
        }
    }

    fun retrieveAdtouch(adKey: String): AdtouchDTO? {
        return adtouchRepository.findByAdKey(adKey)?.let {
            AdtouchDTO(it.adKey, it.trackerId, it.createdAt)
        } ?: throw AdtouchNotFoundException("No Adtouch found for the passed in adKey : $adKey")
    }
}