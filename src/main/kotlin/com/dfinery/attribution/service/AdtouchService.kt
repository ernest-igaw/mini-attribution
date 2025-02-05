package com.dfinery.attribution.service

import com.dfinery.attribution.dto.AdtouchDTO
import com.dfinery.attribution.entity.Adtouch
import com.dfinery.attribution.exception.AdtouchNotFoundException
import com.dfinery.attribution.repository.AdtouchRepository
import com.dfinery.attribution.util.datetime.DateTimeUtil
import org.springframework.stereotype.Service
import mu.KLogging

@Service
class AdtouchService(
    val adtouchRepository: AdtouchRepository
) {
    companion object : KLogging()

    fun generateAdtouch(adtouchDTO: AdtouchDTO): AdtouchDTO {
        val createdTime = DateTimeUtil.getCurrentTimestamp().toString()

        val adtouchEntity = adtouchDTO.let {
            Adtouch(trackerId = it.trackerId, createdAt = createdTime)
        }

        adtouchRepository.save(adtouchEntity)
        logger.info("Saved Adtouch : $adtouchEntity")

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