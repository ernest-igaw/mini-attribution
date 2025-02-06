package com.dfinery.attribution.api.controller

import com.dfinery.attribution.common.dto.AdtouchDTO
import com.dfinery.attribution.common.exception.AdtouchNotFoundException
import com.dfinery.attribution.api.service.AdtouchService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

// Adtouch API
@RestController
@RequestMapping("/v1/adtouch")
@Validated
class AdtouchController(val adtouchService: AdtouchService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun generateAdtouch(@RequestBody @Valid adtouchDTO: AdtouchDTO): AdtouchDTO {
        return adtouchService.generateAdtouch(adtouchDTO)
    }

    @GetMapping("/retrieve")
    fun retrieveAdtouch(@RequestParam("adKey") adKey: String): AdtouchDTO? {
        return adtouchService.retrieveAdtouch(adKey) ?: throw AdtouchNotFoundException("No Adtouch found for the passed in adKey : $adKey")
    }
}