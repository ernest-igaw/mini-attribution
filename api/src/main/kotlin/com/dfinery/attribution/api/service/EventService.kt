package com.dfinery.attribution.api.service

import com.dfinery.attribution.common.dto.EventDTO
import com.dfinery.attribution.common.exception.InvalidEventTypeException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class EventService(
    private val firstOpenService: FirstOpenService,
    private val purchaseService: PurchaseService,
    @Value("\${variables.event.firstopen.type}")
    private val firstOpenEventType: String,
    @Value("\${variables.event.purchase.type}")
    private val purchaseEventType: String
) {
    fun addEvent(eventDTO: EventDTO): EventDTO {
        return when (eventDTO.eventType) {
            firstOpenEventType -> firstOpenService.addFirstOpenEvent(eventDTO)
            purchaseEventType -> purchaseService.addPurchaseEvent(eventDTO)
            else -> throw InvalidEventTypeException("Invalid event type: ${eventDTO.eventType}")
        }
    }
}