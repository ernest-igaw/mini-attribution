package com.dfinery.attribution.api.controller

import com.dfinery.attribution.api.model.response.ResponseDTO
import com.dfinery.attribution.api.service.AdtouchService
import com.dfinery.attribution.api.util.adtouchDTO
import com.dfinery.attribution.common.dto.AdtouchDTO
import com.dfinery.attribution.common.util.datetime.DateTimeUtil.Companion.getCurrentTimestamp
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [AdtouchController::class])
@AutoConfigureWebTestClient
class AdtouchControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var adtouchServiceMockk: AdtouchService

    @Test
    fun addAdtouch() {
        val adtouchDTO = AdtouchDTO(adKey = null, trackerId = "123", createdAt = null)

        every { adtouchServiceMockk.addAdtouch(any()) } returns adtouchDTO(
            adKey = "MyFirstTestAdKey",
            trackerId = "123",
            createdAt = getCurrentTimestamp().toString()
        )

        val responseDTO = webTestClient
            .post()
            .uri("/v1/adtouch")
            .bodyValue(adtouchDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(ResponseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            responseDTO!!.status == 201
        }
    }

    @Test
    fun addAdtouchValidation() {
        val adtouchDTO = AdtouchDTO(null, "", null)

        every { adtouchServiceMockk.addAdtouch(any()) } returns adtouchDTO(
            adKey = "MyFirstTestAdKey",
            trackerId = "",
            createdAt = getCurrentTimestamp().toString()
        )

        val responseDTO = webTestClient
            .post()
            .uri("/v1/adtouch")
            .bodyValue(adtouchDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("AdtouchDTO.trackerId must not be blank", responseDTO!!)
    }
}