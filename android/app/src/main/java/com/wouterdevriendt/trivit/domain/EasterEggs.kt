package com.wouterdevriendt.trivit.domain

object EasterEggs {

    private val messages = mapOf(
        42 to "The answer to life, the universe, and everything",
        69 to "Nice.",
        100 to "Century!",
        404 to "Not found... oh wait, there it is!",
        420 to "Blaze it",
        666 to "The number of the beast",
        777 to "Jackpot!",
        1000 to "Grand!",
        1337 to "L33T!",
        9001 to "It's over 9000!",
        12345 to "That's the combination on my luggage!"
    )

    fun getMessage(count: Int): String? = messages[count]

    fun getAllMessages(): Map<Int, String> = messages
}
