package de.scaramangado.racetimebingobot.api

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("racetime.api")
class ApiProperties(

    var baseUrl: String? = null,
    var websocketBase: String? = null
)