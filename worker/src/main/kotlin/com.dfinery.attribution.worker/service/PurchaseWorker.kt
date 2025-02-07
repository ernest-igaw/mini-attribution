package com.dfinery.attribution.worker.service

import com.dfinery.attribution.common.entity.Profile
import com.dfinery.attribution.common.exception.PostbackFailedException
import com.dfinery.attribution.common.util.datetime.DateTimeUtil
import com.dfinery.attribution.common.util.uuid.UUIDGenerator
import com.dfinery.attribution.worker.repository.ProfileRepository
import com.fasterxml.jackson.databind.JsonNode
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class PurchaseWorker(
    val profileRepository: ProfileRepository,
    @Value("\${variables.partners}")
    val partners: String
) {
    companion object : KLogging()

    fun parsePurchase(payload: JsonNode) {
        logger.info("Received Purchase : $payload")
        val adId = payload["adId"].asText()
        val logId = payload["logId"].asText()
        val adKey = payload["adKey"].asText()
//        val items = payload["items"].asIterable().map {
//            ItemDTO(
//                null,
//                it["productId"].asText(),
//                it["productName"].asText(),
//                it["productPrice"].asText()
//            )
//        }
        val items = payload["items"].asIterable().joinToString("+") {
            it["productId"].asText()
        }

        retrieveAttribution(adId, items)
    }

    private fun retrieveAttribution(adId: String, items: String) {
        profileRepository.findByAdId(adId)?.let {
            logger.info("Retrieved Profile : $it")
            postback(it, items)
        } ?: logger.info("No Profile found for the passed in adId : $adId")
    }

    private fun postback(profile: Profile, items: String) {
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
        logger.info("Postback GET http://ci.adbrix.io:5055/api/Postback/transaction_id=$transactionId&partner=$partnerId&Adid=${profile.adId}&eventName=Purchase&datetime=$dateTime&trackerId=${profile.trackerId}&items=${items}")

        webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/postback/transaction_id=$transactionId&partner=$partnerId&Adid=${profile.adId}&eventName=firstOpen&datetime=$dateTime&trackerId=${profile.trackerId}&items=${items}")
                    .build()
            }
            .retrieve()
            .bodyToMono(String::class.java)
            .onErrorResume { error ->
                logger.error("Postback failed : ${error.message}")
                throw PostbackFailedException("Postback failed")
            }
            .block()
            ?.let {
                logger.info("Postback response : $it")
            } ?: throw PostbackFailedException("Postback failed")
    }
}