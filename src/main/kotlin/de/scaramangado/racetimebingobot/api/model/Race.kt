package de.scaramangado.racetimebingobot.api.model

import java.time.Duration
import java.time.Instant

data class Race(
    var version: Int?,
    var name: String,
    var category: Category?,
    var status: RaceStatus,
    var url: String,
    var dataUrl: String,
    var websocketUrl: String?,
    var websocketBotUrl: String?,
    var websocketOauthUrl: String?,
    var goal: RaceGoal,
    var info: String?,
    var entrantsCount: Int?,
    var entrantsCountInactive: Int?,
    var entrants: List<Entrant>?,
    var openedAt: Instant,
    var startDelay: Duration?,
    var startedAt: Instant?,
    var endedAt: Instant?,
    var cancelledAt: Instant?,
    var timeLimit: Duration,
    var openedBy: User?,
    var monitors: Set<User>?,
    var recordable: Boolean?,
    var recorded: Boolean?,
    var recordedBy: User?,
    var allowComments: Boolean?,
    var allowMidRaceChat: Boolean?,
    var allowNonEntrantChat: Boolean?,
    var chatMessageDelay: Duration?
)
