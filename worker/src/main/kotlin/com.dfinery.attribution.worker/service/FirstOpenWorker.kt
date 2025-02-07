package com.dfinery.attribution.worker.service

import com.dfinery.attribution.common.dto.ProfileDTO
import com.dfinery.attribution.common.entity.Profile
import com.dfinery.attribution.common.exception.AdtouchNotFoundException
import com.dfinery.attribution.common.exception.PostbackFailedException
import com.dfinery.attribution.common.util.datetime.DateTimeUtil
import com.dfinery.attribution.common.util.uuid.UUIDGenerator
import com.dfinery.attribution.worker.repository.AdtouchRepository
import com.dfinery.attribution.worker.repository.ProfileRepository
import com.fasterxml.jackson.databind.JsonNode
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class FirstOpenWorker(
    val adtouchRepository: AdtouchRepository,
    val profileRepository: ProfileRepository,
    @Value("\${variables.event.firstopen.LBWindows.interval}")
    val lbWindowsInterval: Long,
    @Value("\${variables.partners}")
    val partners: String
) {
    companion object : KLogging()

    fun parseFirstOpen(payload: JsonNode): ProfileDTO {
        logger.info("Received FirstOpen : $payload")
        val adId = payload["adId"].asText()
        val logId = payload["logId"].asText()
        val adKey = payload["adKey"].asText()

        return generateAttribution(adId, logId, adKey)
    }

    private fun generateAttribution(adId: String, logId: String, adKey: String): ProfileDTO {
        val adtouch = adtouchRepository.findByAdKey(adKey)
            ?: throw AdtouchNotFoundException("No Adtouch found for the passed in adKey : $adKey")
        logger.info("Retrieved Adtouch : $adtouch")

        val timeInterval = logId.split(":")[0].toLong() - adtouch.createdAt.toLong()
        val profileEntity = if (timeInterval > lbWindowsInterval) {
            logger.info("Time interval is greater than the Lookback Window Interval : $timeInterval")
            Profile(adId, logId, null, null)
        } else {
            Profile(adId, logId, adKey, adtouch.trackerId)
        }

        profileRepository.save(profileEntity)

        return postback(profileEntity)
    }

    private fun postback(profile: Profile): ProfileDTO {
        val webClient = WebClient.builder()
            .baseUrl("http://ci.adbrix.io:5055/api")
            .build()

        val partnerId = profile.trackerId?.let {
            partners.split(",")
                .map { it.split(":") }
                .firstOrNull { it.getOrNull(1) == profile.trackerId }
                ?.getOrNull(0)
                ?: ""
        }

        val transactionId = UUIDGenerator.createRandomStringUUID()
        val dateTime = DateTimeUtil.getCurrentTimestamp().toString()
        logger.info("Postback GET http://ci.adbrix.io:5055/api/Postback/transaction_id=$transactionId&partner=$partnerId&Adid=${profile.adId}&eventName=FirstOpen&datetime=$dateTime&trackerId=${profile.trackerId}")

        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/postback/transaction_id=$transactionId&partner=$partnerId&Adid=${profile.adId}&eventName=firstOpen&datetime=$dateTime&trackerId=${profile.trackerId}")
                    .build()
            }
            .retrieve()
            .bodyToMono(String::class.java)
            .onErrorResume { error ->
                logger.error("Postback failed : ${error.message}")
                throw PostbackFailedException("Postback failed")
            }
            .block()
            ?: throw PostbackFailedException("Postback failed")
        logger.info("Postback response : $response")
        return profile.let {
            ProfileDTO(it.adId, it.firstOpenLogId, it.adKey, it.trackerId)
        }
    }
}