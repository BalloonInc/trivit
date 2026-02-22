package com.wouterdevriendt.trivit

import com.wouterdevriendt.trivit.data.model.TallyEvent
import com.wouterdevriendt.trivit.data.model.Trivit
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class TrivitModelTest {

    @Test
    fun `trivit has correct defaults`() {
        val trivit = Trivit(name = "Test Counter")
        assertEquals("Test Counter", trivit.name)
        assertEquals(0, trivit.count)
        assertEquals(0, trivit.colorIndex)
        assertEquals(0, trivit.sortOrder)
        assertFalse(trivit.isExpanded)
        assertNull(trivit.deletedAt)
    }

    @Test
    fun `trivit preserves custom values`() {
        val trivit = Trivit(
            id = 42,
            name = "Custom",
            count = 100,
            colorIndex = 5,
            sortOrder = 3,
            isExpanded = true
        )
        assertEquals(42L, trivit.id)
        assertEquals("Custom", trivit.name)
        assertEquals(100, trivit.count)
        assertEquals(5, trivit.colorIndex)
        assertEquals(3, trivit.sortOrder)
        assert(trivit.isExpanded)
    }

    @Test
    fun `trivit copy works correctly`() {
        val original = Trivit(name = "Original", count = 5)
        val copy = original.copy(name = "Copy", count = 10)
        assertEquals("Copy", copy.name)
        assertEquals(10, copy.count)
        assertEquals(original.colorIndex, copy.colorIndex)
    }

    @Test
    fun `tally event has correct defaults`() {
        val event = TallyEvent(trivitId = 1)
        assertEquals(1L, event.trivitId)
        assertEquals(1, event.delta)
    }

    @Test
    fun `tally event with negative delta`() {
        val event = TallyEvent(trivitId = 1, delta = -1)
        assertEquals(-1, event.delta)
    }
}
