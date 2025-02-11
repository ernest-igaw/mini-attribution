package com.dfinery.attribution.api.controller

import com.dfinery.attribution.api.model.response.ResponseDTO
import com.dfinery.attribution.api.repository.AdtouchRepository
import com.dfinery.attribution.api.util.adtouchEntityList
import com.dfinery.attribution.api.util.firstOpenList
import com.dfinery.attribution.common.dto.EventDTO
import com.dfinery.attribution.common.dto.ItemDTO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test-worker")
@AutoConfigureWebTestClient
class EventControllerIntgTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var adtouchRepository: AdtouchRepository

    @BeforeEach
    fun setup() {
        adtouchRepository.deleteAllItems()
        adtouchEntityList().forEach { adtouchRepository.save(it) }
    }

    @Test
    fun addRegularFirstOpenEvent() {
        val responseDTO = webTestClient
            .post()
            .uri("/v1/event")
            .bodyValue(
                EventDTO(
                    adId = "MyFirstTestAdId",
                    eventType = "FirstOpen",
                    logId = null,
                    adKey = "MyFirstFavoriteAdKey",
                    items = null
                )
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

    @Test
    fun addAbnormalFirstOpenEvent() {
        val responseDTO = webTestClient
            .post()
            .uri("/v1/event")
            .bodyValue(
                EventDTO(
                    adId = "MyFirstTestAdId",
                    eventType = "FirstOpen",
                    logId = null,
                    adKey = null,
                    items = null
                )
            )
            .exchange()
            .expectStatus().isNotFound
            .expectBody(ResponseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            responseDTO!!.status == 404 && responseDTO.message.contains("The adKey is required for FirstOpen event.")
        }
    }

    @Test
    fun addRegularPurchaseEvent() {
        initFirstOpen()

        val responseDTO = webTestClient
            .post()
            .uri("/v1/event")
            .bodyValue(
                EventDTO(
                    adId = "MyFirstFavoriteAdId",
                    eventType = "Purchase",
                    logId = null,
                    items = listOf(
                        ItemDTO(
                            productId = "MyFirstFavoriteProductId",
                            productName = "MyFirstFavoriteProduct",
                            productPrice = "10000"
                        ),
                        ItemDTO(
                            productId = "MySecondFavoriteProductId",
                            productName = "MySecondFavoriteProduct",
                            productPrice = "20000"
                        ),
                    )
                )
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

    @Test
    fun addAbnormalPurchaseEvent() {
        initFirstOpen()

        val responseDTO = webTestClient
            .post()
            .uri("/v1/event")
            .bodyValue(
                EventDTO(
                    adId = "MySecondFavoriteAdId",
                    eventType = "Purchase",
                    logId = null,
                    items = null
                )
            )
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(ResponseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            responseDTO!!.status == 400 && responseDTO.message.contains("The items are required for Purchase event")
        }
    }

    private fun initFirstOpen() {
        firstOpenList().forEach {
            webTestClient
                .post()
                .uri("/v1/event")
                .bodyValue(it)
                .exchange()
                .expectStatus().isCreated
        }
    }
}