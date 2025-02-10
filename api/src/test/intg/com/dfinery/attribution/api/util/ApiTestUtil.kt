package com.dfinery.attribution.api.util

import com.dfinery.attribution.common.dto.EventDTO
import com.dfinery.attribution.common.entity.Adtouch
import com.dfinery.attribution.common.util.datetime.DateTimeUtil.Companion.getCurrentTimestamp
import com.dfinery.attribution.common.util.uuid.UUIDGenerator.Companion.createRandomPartitionKey

fun adtouchEntityList() = listOf(
    Adtouch(adKey = "MyFirstFavoriteAdKey", trackerId = "123", createdAt = getCurrentTimestamp().toString()),
    Adtouch(adKey = "MySecondFavoriteAdKey", trackerId = "ABC", createdAt = getCurrentTimestamp().toString()),
    Adtouch(adKey = "MyThirdFavoriteAdKey", trackerId = "456", createdAt = getCurrentTimestamp().toString())
)

fun firstOpenList() = listOf(
    EventDTO(
        adId = "MyFirstFavoriteAdId",
        eventType = "FirstOpen",
        logId = null,
        adKey = "MyFirstFavoriteAdKey",
        items = null
    ),
    EventDTO(
        adId = "MySecondFavoriteAdId",
        eventType = "FirstOpen",
        logId = "${getCurrentTimestamp()}:${createRandomPartitionKey()}",
        adKey = "MySecondFavoriteAdKey"
    ),
    EventDTO(
        adId = "MyThirdFavoriteAdId",
        eventType = "FirstOpen",
        logId = "MyThirdFavoriteLogId",
        adKey = "MyThirdFavoriteAdKey",
        items = null
    ),
)
