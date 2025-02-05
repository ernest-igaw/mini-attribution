package com.dfinery.attribution.util.uuid

import java.util.*

class UUIDGenerator {
    companion object {
        fun createRandomPartitionKey(): String {
            return UUID.randomUUID().toString().replace("-", "").slice(0..8)
        }

        fun createRandomStringUUID(): String {
            return UUID.randomUUID().toString()
        }
    }
}