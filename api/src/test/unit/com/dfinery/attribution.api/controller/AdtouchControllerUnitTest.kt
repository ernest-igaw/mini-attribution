package com.dfinery.attribution.api.controller

import com.dfinery.attribution.api.service.AdtouchService
import com.dfinery.attribution.common.dto.AdtouchDTO
import com.dfinery.attribution.common.util.datetime.DateTimeUtil.Companion.getCurrentTimestamp
import com.ninjasquad.springmockk.MockkBean
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
        val adtouchDTO =
            AdtouchDTO(adKey = "MyFirstTestAdKey", trackerId = "123", createdAt = getCurrentTimestamp().toString())
    }
}