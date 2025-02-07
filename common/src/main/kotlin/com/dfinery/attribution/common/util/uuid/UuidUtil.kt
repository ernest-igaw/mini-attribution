package com.dfinery.attribution.common.util.uuid

import java.util.*

class UUIDGenerator {
    companion object {
        fun createRandomStringUUID(): String {
            return UUID.randomUUID().toString()
        }

        fun createRandomPartitionKey(): String {
            return UUID.randomUUID().toString().replace("-", "").slice(0..8)
        }
    }
}