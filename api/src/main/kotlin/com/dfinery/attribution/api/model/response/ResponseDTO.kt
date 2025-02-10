package com.dfinery.attribution.api.model.response

data class ResponseDTO(
    var status: Int = 500,
    var message: String = "Internal Server Error"
)