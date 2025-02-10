package com.dfinery.attribution.api.service

import com.dfinery.attribution.api.repository.AdtouchRepository
import com.dfinery.attribution.common.dto.AdtouchDTO
import com.dfinery.attribution.common.entity.Adtouch
import com.dfinery.attribution.common.util.datetime.DateTimeUtil
import com.dfinery.attribution.common.util.uuid.UUIDGenerator
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class AdtouchService(
    private val adtouchRepository: AdtouchRepository
) {
    companion object : KLogging()

    fun addAdtouch(adtouchDTO: AdtouchDTO): AdtouchDTO {
        val adtouchEntity = adtouchDTO.let {
            Adtouch(
                it.adKey ?: UUIDGenerator.createRandomPartitionKey(),
                it.trackerId,
                it.createdAt?.takeIf { unixTime -> unixTime.length >= 12 } ?: it.createdAt?.plus("000")
                ?: DateTimeUtil.getCurrentTimestamp().toString()
            )
        }

        logger.info("Saved Adtouch : $adtouchEntity")
        adtouchRepository.save(adtouchEntity)

        return adtouchEntity.let {
            AdtouchDTO(it.adKey, it.trackerId, it.createdAt)
        }
    }
}