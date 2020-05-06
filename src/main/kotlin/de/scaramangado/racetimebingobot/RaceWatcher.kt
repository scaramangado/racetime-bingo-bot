package de.scaramangado.racetimebingobot

import de.scaramangado.racetimebingobot.api.ApiProperties
import de.scaramangado.racetimebingobot.api.client.RaceService
import de.scaramangado.racetimebingobot.oauth.OAuthService
import de.scaramangado.racetimebingobot.racing.RaceConnection
import org.springframework.boot.ApplicationArguments
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class RaceWatcher(private val raceService: RaceService,
                  private val oAuthService: OAuthService,
                  private val apiProperties: ApiProperties) {

  private val racesJoined = mutableListOf<String>()

  @Scheduled(fixedDelay = 5000)
  fun lookForRaces() {

    raceService.getOpenRacesOfCategory("oot")
        .mapNotNull { raceService.getRaceInfo(it.dataUrl) }
        .filter { it.goal.name == "Bingo" }
        .mapNotNull { it.websocketBotUrl }
        .filter { it !in racesJoined }
        .forEach {
          racesJoined.add(it)
          RaceConnection("${apiProperties.websocketBase}${it}", oAuthService.getBearerToken())
        }
  }
}
