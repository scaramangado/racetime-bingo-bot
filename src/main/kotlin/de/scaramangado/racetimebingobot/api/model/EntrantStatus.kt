package de.scaramangado.racetimebingobot.api.model

data class EntrantStatus(
    var value: Status? = null,
    var verboseValue: String? = null,
    var helpText: String? = null
) {

  enum class Status {
    REQUESTED, INVITED, DECLINED, READY, NOT_READY, IN_PROGRESS, DONE, DNF, DQ
  }
}
