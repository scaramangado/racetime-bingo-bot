package de.scaramangado.racetimebingobot.racing

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import de.scaramangado.racetimebingobot.api.JsonConfiguration
import de.scaramangado.racetimebingobot.api.model.Race
import de.scaramangado.racetimebingobot.api.model.RaceStatus
import de.scaramangado.racetimebingobot.api.model.User
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import java.net.URI
import java.time.Instant
import java.util.UUID
import kotlin.random.Random

class RaceConnection(raceEndpoint: String, token: String) : WebSocketHandler {

  private var raceStarted: Boolean = false
  private var mode = Mode.JP
  private lateinit var session: WebSocketSession

  val gson = JsonConfiguration().gson()

  private enum class Mode(val version: String, val mode: String = "normal") {
    JP("v10.0"),
    EN("v9.5.1"),
    BLACKOUT("v10.0", "blackout"),
    SHORT("v10.0", "short");
  }

  init {
    StandardWebSocketClient()
        .doHandshake(this,
                     WebSocketHttpHeaders().also { it.add("Authorization", "Bearer $token") },
                     URI.create(raceEndpoint))
  }

  override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {

    val payload = message.payload

    if (payload !is String || payload.contains("\"error\"")) {
      return
    }

    val racetimeMessage = gson.jsonToMap(payload)

    when (racetimeMessage["type"]) {
      "chat.message" -> handleChatMessage(racetimeMessage.toChatMessage())
      "race.data" -> racetimeMessage.toRace()?.let { handleRaceEvent(it) }
      else -> return
    }
  }

  override fun afterConnectionEstablished(session: WebSocketSession) {
    this.session = session
    session.sendChatMessage("Welcome to OoT Bingo. I will generate a card and a filename at the start of the race.")
    session.sendChatMessage("Commands: '!mode en', '!mode jp', '!mode blackout', '!mode short' and '!nobingo'")
    session.sendChatMessage("Current mode: JP")
  }

  private fun handleChatMessage(message: ChatMessage) {
    when (message.messagePlain.toLowerCase()) {
      "!mode jp" -> {
        mode = Mode.JP
        session.sendChatMessage("New mode: JP")
      }
      "!mode en" -> {
        mode = Mode.EN
        session.sendChatMessage("New mode: EN")
      }
      "!mode blackout" -> {
        mode = Mode.BLACKOUT
        session.sendChatMessage("New mode: BLACKOUT")
      }
      "!mode short" -> {
        mode = Mode.SHORT
        session.sendChatMessage("New mode: SHORT")
      }
      "!nobingo" -> {
        raceStarted = true
        session.sendChatMessage("No Board or filename will be generated! This action cannot be reverted.")
      }
    }
  }

  private fun handleRaceEvent(race: Race) {

    if (raceStarted || race.status.value != RaceStatus.Status.IN_PROGRESS) {
      return
    }

    raceStarted = true

    val goal = "https://ootbingo.github.io/bingo/${mode.version}/bingo.html?seed=${generateSeed()}&mode=${mode.mode}"

    session.setGoal(goal)
    session.sendChatMessage("Filename: ${generateFilename()} @entrants")
    session.sendChatMessage("Goal: $goal @entrants")
  }

  private fun generateSeed() = Random.nextInt(1,1_000_000)

  private fun generateFilename(): String {

    val charPool : List<Char> = ('A'..'Z').toList()

    return (1..2)
        .map { Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
  }

  //<editor-fold desc="Interface">

  override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
    System.err.println(exception.message)
  }

  override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
    println("connection closed")
  }

  override fun supportsPartialMessages(): Boolean {
    return false
  }

  //</editor-fold>

  @Suppress("UNCHECKED_CAST")
  private fun Gson.jsonToMap(json: String): LinkedTreeMap<String, Any> =
      fromJson(json, Object::class.java) as LinkedTreeMap<String, Any>

  private fun LinkedTreeMap<String, Any>.toChatMessage(): ChatMessage {
    return get("message")?.let { gson.fromJson(gson.toJson(it), ChatMessage::class.java) } ?: ChatMessage()
  }

  private fun LinkedTreeMap<String, Any>.toRace(): Race? {
    return get("race")?.let { gson.fromJson(gson.toJson(it), Race::class.java) }
  }

  private fun WebSocketSession.sendChatMessage(message: String) {
    sendMessage(TextMessage("""{
        "action": "message",
        "data": {
          "message": "$message",
          "guid": "${UUID.randomUUID()}"
        }
      }"""))
  }

  private fun WebSocketSession.setGoal(goal: String) {
    sendMessage(TextMessage("""{
        "action": "setinfo",
        "data": {
          "info": "$goal"
        }
      }""".trimIndent()))
  }
}

data class ChatMessage(
    var id: String = "",
    var user: User? = null,
    var bot: String? = null,
    var postedAt: Instant = Instant.EPOCH,
    var message: String = "",
    var messagePlain: String = "",
    var highlight: Boolean = false,
    var isBot: Boolean = false,
    var isSystem: Boolean = false
)
