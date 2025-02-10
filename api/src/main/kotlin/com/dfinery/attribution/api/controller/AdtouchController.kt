package com.dfinery.attribution.api.controller

import com.dfinery.attribution.api.model.response.ResponseDTO
import com.dfinery.attribution.api.service.AdtouchService
import com.dfinery.attribution.common.dto.AdtouchDTO
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

// Adtouch API
@RestController
@RequestMapping("/v1/adtouch")
@Validated
class AdtouchController(val adtouchService: AdtouchService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun generateAdtouch(@RequestBody @Valid adtouchDTO: AdtouchDTO): ResponseDTO {
        adtouchService.generateAdtouch(adtouchDTO)
        return ResponseDTO(HttpStatus.CREATED.value(), HttpStatus.CREATED.reasonPhrase)
    }
}