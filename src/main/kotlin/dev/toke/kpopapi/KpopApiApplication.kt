package dev.toke.kpopapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KpopApiApplication

fun main(args: Array<String>) {
	runApplication<KpopApiApplication>(*args)
}
