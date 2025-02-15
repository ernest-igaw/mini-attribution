package com.dfinery.attribution.api.controller

import com.dfinery.attribution.api.model.response.ResponseDTO
import com.dfinery.attribution.api.service.EventService
import com.dfinery.attribution.common.dto.EventDTO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

// Event API
@RestController
@RequestMapping("/v1/event")
@Validated
class EventController(
    val eventService: EventService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addEvent(@RequestBody @Valid eventDTO: EventDTO): ResponseDTO {
        eventService.addEvent(eventDTO)
        return ResponseDTO(HttpStatus.CREATED.value(), HttpStatus.CREATED.reasonPhrase)
    }
}