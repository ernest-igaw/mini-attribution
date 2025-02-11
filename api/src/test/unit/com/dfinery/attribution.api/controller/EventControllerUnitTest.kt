package com.dfinery.attribution.api.controller

import com.dfinery.attribution.api.model.response.ResponseDTO
import com.dfinery.attribution.api.service.EventService
import com.dfinery.attribution.api.util.eventDTO
import com.dfinery.attribution.common.dto.EventDTO
import com.dfinery.attribution.common.exception.InvalidEventTypeException
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [EventController::class])
@AutoConfigureWebTestClient
class EventControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var eventServiceMockk: EventService

    @Test
    fun addEvent() {
        val eventDTO = EventDTO("MyFirstTestAdId", "FirstOpen", null)

        every { eventServiceMockk.addEvent(any()) } returns when (eventDTO.eventType) {
            "FirstOpen" -> eventDTO(
                adId = "MyFirstTestAdKey",
                eventType = "FirstOpen",
                logId = "MyFirstTestLogId"
            )

            "Purchase" -> eventDTO(
                adId = "MyFirstTestAdKey",
                eventType = "Purchase",
                logId = "MyFirstTestLogId"
            )

            else -> throw InvalidEventTypeException("Invalid event type: ${eventDTO.eventType}")
        }

        val savedResponseDTO = webTestClient
            .post()
            .uri("/v1/event")
            .bodyValue(eventDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(ResponseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            savedResponseDTO!!.status == 201
        }
    }

    @Test
    fun addEventValidation() {
        val eventDTO = EventDTO("MyFirstTestAdId", "", null)

        every { eventServiceMockk.addEvent(any()) } returns eventDTO(
            adId = "MyFirstTestAdId",
            eventType = "",
            logId = "MyFirstTestLogId"
        )

        val response = webTestClient
            .post()
            .uri("/v1/event")
            .bodyValue(eventDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("EventDTO.eventType must not be blank", response)
    }
}