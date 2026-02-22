package com.wouterdevriendt.trivit

import com.wouterdevriendt.trivit.domain.EasterEggs
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class EasterEggsTest {

    @Test
    fun `returns correct message for 42`() {
        assertEquals(
            "The answer to life, the universe, and everything",
            EasterEggs.getMessage(42)
        )
    }

    @Test
    fun `returns correct message for 69`() {
        assertEquals("Nice.", EasterEggs.getMessage(69))
    }

    @Test
    fun `returns correct message for 100`() {
        assertEquals("Century!", EasterEggs.getMessage(100))
    }

    @Test
    fun `returns correct message for 404`() {
        assertEquals(
            "Not found... oh wait, there it is!",
            EasterEggs.getMessage(404)
        )
    }

    @Test
    fun `returns correct message for 420`() {
        assertEquals("Blaze it", EasterEggs.getMessage(420))
    }

    @Test
    fun `returns correct message for 666`() {
        assertEquals("The number of the beast", EasterEggs.getMessage(666))
    }

    @Test
    fun `returns correct message for 777`() {
        assertEquals("Jackpot!", EasterEggs.getMessage(777))
    }

    @Test
    fun `returns correct message for 1000`() {
        assertEquals("Grand!", EasterEggs.getMessage(1000))
    }

    @Test
    fun `returns correct message for 1337`() {
        assertEquals("L33T!", EasterEggs.getMessage(1337))
    }

    @Test
    fun `returns correct message for 9001`() {
        assertEquals("It's over 9000!", EasterEggs.getMessage(9001))
    }

    @Test
    fun `returns correct message for 12345`() {
        assertEquals(
            "That's the combination on my luggage!",
            EasterEggs.getMessage(12345)
        )
    }

    @Test
    fun `returns null for non-easter-egg count`() {
        assertNull(EasterEggs.getMessage(1))
        assertNull(EasterEggs.getMessage(50))
        assertNull(EasterEggs.getMessage(999))
    }

    @Test
    fun `all messages map has 11 entries`() {
        assertEquals(11, EasterEggs.getAllMessages().size)
    }
}
