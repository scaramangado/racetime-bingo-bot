package de.scaramangado.racetimebingobot.api.model

import java.time.Duration
import java.time.Instant

data class Entrant(
    var user: User? = null,
    var status: EntrantStatus? = null,
    var finishTime: Duration? = null,
    var finishedAt: Instant? = null,
    var place: Int? = null,
    var placeOrdinal: String? = null,
    var score: Int? = null,
    var scoreChange: Int? = null,
    var comment: String? = null,
    var streamLive: Boolean? = null,
    var streamOverride: Boolean? = null
)
