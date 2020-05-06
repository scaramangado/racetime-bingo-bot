package de.scaramangado.racetimebingobot.api.client

import de.scaramangado.racetimebingobot.api.ApiProperties
import de.scaramangado.racetimebingobot.api.model.Category
import de.scaramangado.racetimebingobot.api.model.Race
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.getForEntity

@Service
class RaceService(private val properties: ApiProperties, private val restTemplateBuilder: RestTemplateBuilder) {

  fun getOpenRacesOfCategory(categorySlug: String): List<Race> {
    val category =
        restTemplateBuilder.build()
            .getForEntity<Category>("${properties.baseUrl}/$categorySlug/data")

    return category.body?.currentRaces ?: emptyList()
  }

  fun getRaceInfo(dataUrl: String): Race? =
      restTemplateBuilder.build().getForEntity<Race>("${properties.baseUrl}$dataUrl").body
}
