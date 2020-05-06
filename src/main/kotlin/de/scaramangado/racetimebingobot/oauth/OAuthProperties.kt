package de.scaramangado.racetimebingobot.oauth

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("racetime.oauth")
data class OAuthProperties (
    var clientId: String = "",
    var clientSecret: String = ""
)
