package com.dfinery.attribution.service

import com.dfinery.attribution.dto.EventDTO
import com.dfinery.attribution.dto.ProfileDTO
import com.dfinery.attribution.exception.InvalidEventTypeException
import com.dfinery.attribution.exception.ProfileNotFoundException
import com.dfinery.attribution.repository.ProfileRepository
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class EventService (
    private val profileRepository: ProfileRepository,
    private val firstOpenService: FirstOpenService,
    private val purchaseService: PurchaseService,
    @Value("\${variables.event.firstopen.type}")
    private val firstOpenEventType: String,
    @Value("\${variables.event.purchase.type}")
    private val purchaseEventType: String
) {
    companion object : KLogging()

    fun generateEvent(eventDTO: EventDTO): EventDTO {
        return when (eventDTO.eventType) {
            firstOpenEventType -> firstOpenService.generateFirstOpenEvent(eventDTO)
            purchaseEventType -> purchaseService.generatePurchaseEvent(eventDTO)
            else -> throw InvalidEventTypeException("Invalid event type: ${eventDTO.eventType}")
        }
    }

    fun retrieveProfile(adId: String): ProfileDTO {
        val profile = profileRepository.findByAdId(adId)

        return profile?.let {
            ProfileDTO(it.adId, it.firstOpenLogId, it.adKey, it.trackerId)
        } ?: throw ProfileNotFoundException("No Profile found for the passed in adId : $adId")
    }
}