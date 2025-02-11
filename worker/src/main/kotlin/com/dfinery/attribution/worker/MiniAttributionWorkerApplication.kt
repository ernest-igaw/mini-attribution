package com.dfinery.attribution.worker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MiniAttributionWorkerApplication

fun main(args: Array<String>) {
	runApplication<MiniAttributionWorkerApplication>(*args)
}
