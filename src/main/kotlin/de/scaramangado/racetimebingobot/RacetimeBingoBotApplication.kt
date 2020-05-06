package de.scaramangado.racetimebingobot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class RacetimeBingoBotApplication

fun main(args: Array<String>) {
	runApplication<RacetimeBingoBotApplication>(*args)
}
