package com.dfinery.attribution.api.controller

import com.dfinery.attribution.common.dto.EventDTO
import com.dfinery.attribution.api.service.EventService
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// Event API
@RestController
@RequestMapping("/v1/event")
@Validated
class EventController(
    val eventService: EventService
) {
    @PostMapping
    fun generateEvent(@RequestBody @Valid eventDTO: EventDTO): EventDTO {
        return eventService.generateEvent(eventDTO)
    }
}