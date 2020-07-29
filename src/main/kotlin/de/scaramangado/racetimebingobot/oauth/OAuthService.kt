package de.scaramangado.racetimebingobot.oauth

import com.google.gson.Gson
import de.scaramangado.racetimebingobot.api.ApiProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant

@Component
class OAuthService(private val apiProperties: ApiProperties,
                   private val properties: OAuthProperties,
                   private val restTemplateBuilder: RestTemplateBuilder,
                   private val gson: Gson) {

  private var token = OAuthToken("", Instant.EPOCH)

  fun getBearerToken(): String {
    if (token.expired) {
      renewToken()
    }

    return token.token
  }

  private fun renewToken() {

    val headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

    token = properties
        .let { TokenRequest(it.clientId, it.clientSecret) }
        .let {
          restTemplateBuilder.build()
              .postForEntity("${apiProperties.baseUrl}/o/token",
                             HttpEntity(it.toFormData(), headers),
                             TokenResponse::class.java)
              .body
        }
        ?.let {
          OAuthToken(it.accessToken, Instant.now().plusSeconds(it.expiresIn))
        } ?: token
  }

  private data class TokenRequest(
      val clientId: String,
      val clientSecret: String,
      val grantType: String = "client_credentials"
  ) {
    fun toFormData() =
        "client_id=$clientId&client_secret=$clientSecret&grant_type=$grantType"
  }

  private data class TokenResponse(
      val accessToken: String,
      val expiresIn: Long
  )

  private data class OAuthToken(val token: String, val expires: Instant) {
    val expired: Boolean
      get() = Instant.now().let { it.isAfter(expires) || Duration.between(it, expires).toHours() < 2 }
  }
}
