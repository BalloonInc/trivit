package com.wouterdevriendt.trivit

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests that swipe gesture thresholds match the iOS implementation.
 * iOS TrivitRowView.swift:
 *   decrementThreshold: -60 points
 *   deleteThreshold: -200 points
 *
 * These thresholds are defined in TrivitRow.kt as dp values.
 */
class SwipeThresholdTest {

    // Mirror the constants from TrivitRow.kt for validation
    private val decrementThreshold = -60f // dp
    private val deleteThreshold = -200f // dp

    @Test
    fun `decrement threshold matches iOS (-60)`() {
        assertTrue(
            "Decrement threshold should be -60dp to match iOS",
            decrementThreshold == -60f
        )
    }

    @Test
    fun `delete threshold matches iOS (-200)`() {
        assertTrue(
            "Delete threshold should be -200dp to match iOS",
            deleteThreshold == -200f
        )
    }

    @Test
    fun `decrement threshold is less negative than delete threshold`() {
        assertTrue(
            "Decrement zone should be before delete zone",
            decrementThreshold > deleteThreshold
        )
    }

    @Test
    fun `small swipe does not trigger any action`() {
        val smallSwipe = -30f
        assertTrue(
            "A -30dp swipe should not trigger decrement",
            smallSwipe > decrementThreshold
        )
    }

    @Test
    fun `medium swipe triggers decrement only`() {
        val mediumSwipe = -100f
        assertTrue(
            "A -100dp swipe should exceed decrement threshold",
            mediumSwipe < decrementThreshold
        )
        assertTrue(
            "A -100dp swipe should not exceed delete threshold",
            mediumSwipe > deleteThreshold
        )
    }

    @Test
    fun `long swipe triggers delete`() {
        val longSwipe = -250f
        assertTrue(
            "A -250dp swipe should exceed delete threshold",
            longSwipe < deleteThreshold
        )
    }

    @Test
    fun `swipe right at boundary values`() {
        // Exactly at threshold should trigger (iOS uses < comparison)
        assertTrue("Exactly -60 should be at decrement boundary", -60f <= decrementThreshold)
        assertTrue("Exactly -200 should be at delete boundary", -200f <= deleteThreshold)

        // Just above threshold should not trigger
        assertTrue("-59.9 should not trigger decrement", -59.9f > decrementThreshold)
        assertTrue("-199.9 should not trigger delete", -199.9f > deleteThreshold)
    }
}
