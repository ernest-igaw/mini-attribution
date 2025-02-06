package com.dfinery.attribution.common.util.datetime

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class DateTimeUtil {
    companion object {
        /**
         * `getCurrentDateTime(tz)`
         * @param zoneOffset: ZoneOffset: timezone info
         * @return java.time.LocalDateTime: current time with given timezone applied(default is UTC)
         */
        fun getCurrentDateTime(zoneOffset: ZoneOffset = ZoneOffset.UTC): LocalDateTime {
            return LocalDateTime.now(zoneOffset)
        }

        fun getCurrentTimestamp(): Long {
            return System.currentTimeMillis()
        }

        fun convertTimestampToDateTime(timestamp: Long): LocalDateTime {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC)
        }

        /**
         * `getCurrentTimeInstant()`
         * @return java.time.Instant: current time with UTC timezone (ISO-8601)
         * @sample "YYYY-MM-DDThh:mm:ss.SSSZ"
         */
        fun getCurrentTimeInstant(): Instant {
            return Instant.now()
        }
    }
}