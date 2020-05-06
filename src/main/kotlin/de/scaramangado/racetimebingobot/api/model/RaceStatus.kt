package de.scaramangado.racetimebingobot.api.model


data class RaceStatus(
    var value: Status? = null,
    var verboseValue: String? = null,
    var helpText: String? = null
) {

  enum class Status {
    OPEN, INVITATIONAL, PENDING, IN_PROGRESS, FINISHED, CANCELLED;

    companion object {
      @JvmStatic
      fun fromString(key: String): Status? =
        values().findLast {
          it.name.equals(key, ignoreCase = true)
        }
    }
  }
}
