package com.dfinery.attribution.api.controller

import com.dfinery.attribution.api.model.response.ResponseDTO
import com.dfinery.attribution.api.repository.AdtouchRepository
import com.dfinery.attribution.common.dto.AdtouchDTO
import com.dfinery.attribution.common.util.datetime.DateTimeUtil.Companion.getCurrentTimestamp
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test-api")
@AutoConfigureWebTestClient
class AdtouchControllerIntgTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var adtouchRepository: AdtouchRepository

    @BeforeEach
    fun setup() {
        adtouchRepository.deleteAllItems()
    }

    @Test
    fun addAdtouch() {
        val responseDTO = webTestClient
            .post()
            .uri("/v1/adtouch")
            .bodyValue(
                AdtouchDTO(adKey = null, trackerId = "123", createdAt = getCurrentTimestamp().toString())
            )
            .exchange()
            .expectStatus().isCreated
            .expectBody(ResponseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            responseDTO!!.status == 201 && responseDTO.message == "Created"
        }
    }
}