package de.scaramangado.racetimebingobot.api.model

data class Category(
    var name: String? = null,
    var shortName: String? = null,
    var slug: String? = null,
    var url: String? = null,
    var dataUrl: String? = null,
    var image: String? = null,
    var info: String? = null,
    var streamingRequired: Boolean? = null,
    var owner: User? = null,
    var moderators: Set<User>? = null,
    var currentRaces: List<Race>? = null
)
