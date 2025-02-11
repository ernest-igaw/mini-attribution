package com.dfinery.attribution.api.util

import com.dfinery.attribution.common.dto.AdtouchDTO
import com.dfinery.attribution.common.dto.EventDTO
import com.dfinery.attribution.common.dto.ItemDTO
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

fun adtouchDTO(
    adKey: String?,
    trackerId: String,
    createdAt: String?
) = AdtouchDTO(adKey, trackerId, createdAt)

fun eventDTO(
    adId: String,
    eventType: String,
    logId: String?,
    adKey: String? = null,
    items: List<ItemDTO>? = null
) = EventDTO(adId, eventType, logId, adKey, items)