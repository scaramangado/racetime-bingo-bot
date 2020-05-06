package de.scaramangado.racetimebingobot.api.model

data class User(
    var id: String? = null,
    var fullName: String? = null,
    var name: String? = null,
    var discriminator: String? = null,
    var url: String? = null,
    var avatar: String? = null,
    var pronouns: String? = null,
    var flair: String? = null,
    var twitchName: String? = null,
    var twitchChannel: String? = null,
    var canModerate: Boolean? = null
)
