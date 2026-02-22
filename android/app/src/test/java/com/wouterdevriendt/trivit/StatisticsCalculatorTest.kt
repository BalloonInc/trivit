package com.wouterdevriendt.trivit

import com.wouterdevriendt.trivit.data.model.TallyEvent
import com.wouterdevriendt.trivit.domain.StatisticsCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class StatisticsCalculatorTest {

    private fun eventAt(dateTime: LocalDateTime, delta: Int = 1, trivitId: Long = 1L): TallyEvent {
        val timestamp = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return TallyEvent(trivitId = trivitId, delta = delta, timestamp = timestamp)
    }

    private fun eventOnDate(date: LocalDate, hour: Int = 12, delta: Int = 1): TallyEvent {
        return eventAt(date.atTime(hour, 0), delta)
    }

    @Test
    fun `empty events returns zero statistics`() {
        val stats = StatisticsCalculator.calculate(emptyList(), 0)
        assertEquals(0, stats.totalCount)
        assertEquals(0, stats.currentStreak)
        assertEquals(0, stats.longestStreak)
        assertEquals(0, stats.daysTracked)
        assertTrue(stats.hourlyDistribution.isEmpty())
        assertTrue(stats.dayOfWeekDistribution.isEmpty())
        assertTrue(stats.monthlyTrend.isEmpty())
    }

    @Test
    fun `total count uses provided current count`() {
        val events = listOf(
            eventOnDate(LocalDate.now())
        )
        val stats = StatisticsCalculator.calculate(events, 42)
        assertEquals(42, stats.totalCount)
    }

    @Test
    fun `hourly distribution counts events by hour`() {
        val today = LocalDate.now()
        val events = listOf(
            eventAt(today.atTime(9, 0)),
            eventAt(today.atTime(9, 30)),
            eventAt(today.atTime(14, 0)),
            eventAt(today.atTime(14, 15)),
            eventAt(today.atTime(14, 45)),
            eventAt(today.atTime(22, 0))
        )
        val stats = StatisticsCalculator.calculate(events, 6)
        assertEquals(2, stats.hourlyDistribution[9])
        assertEquals(3, stats.hourlyDistribution[14])
        assertEquals(1, stats.hourlyDistribution[22])
    }

    @Test
    fun `hourly distribution ignores decrements`() {
        val today = LocalDate.now()
        val events = listOf(
            eventAt(today.atTime(9, 0), delta = 1),
            eventAt(today.atTime(9, 30), delta = -1),
            eventAt(today.atTime(9, 45), delta = 1)
        )
        val stats = StatisticsCalculator.calculate(events, 1)
        assertEquals(2, stats.hourlyDistribution[9])
    }

    @Test
    fun `day of week distribution counts correctly`() {
        // Create events on specific known days
        // Jan 6, 2025 is a Monday (dayOfWeek.value = 1)
        val monday = LocalDate.of(2025, 1, 6)
        val tuesday = LocalDate.of(2025, 1, 7)
        val wednesday = LocalDate.of(2025, 1, 8)
        val events = listOf(
            eventOnDate(monday),
            eventOnDate(monday, hour = 14),
            eventOnDate(tuesday),
            eventOnDate(wednesday),
            eventOnDate(wednesday, hour = 15),
            eventOnDate(wednesday, hour = 16)
        )
        val stats = StatisticsCalculator.calculate(events, 6)
        assertEquals(2, stats.dayOfWeekDistribution[1]) // Monday
        assertEquals(1, stats.dayOfWeekDistribution[2]) // Tuesday
        assertEquals(3, stats.dayOfWeekDistribution[3]) // Wednesday
    }

    @Test
    fun `monthly trend groups events by month`() {
        val events = listOf(
            eventOnDate(LocalDate.of(2025, 1, 15)),
            eventOnDate(LocalDate.of(2025, 1, 20)),
            eventOnDate(LocalDate.of(2025, 2, 10)),
            eventOnDate(LocalDate.of(2025, 3, 5)),
            eventOnDate(LocalDate.of(2025, 3, 6)),
            eventOnDate(LocalDate.of(2025, 3, 7))
        )
        val stats = StatisticsCalculator.calculate(events, 6)
        assertEquals(2, stats.monthlyTrend["2025-01"])
        assertEquals(1, stats.monthlyTrend["2025-02"])
        assertEquals(3, stats.monthlyTrend["2025-03"])
    }

    @Test
    fun `last 30 days shows activity for recent days`() {
        val today = LocalDate.now()
        val events = listOf(
            eventOnDate(today),
            eventOnDate(today),
            eventOnDate(today.minusDays(1)),
            eventOnDate(today.minusDays(5)),
            eventOnDate(today.minusDays(31)) // should not be included
        )
        val stats = StatisticsCalculator.calculate(events, 5)
        assertEquals(2, stats.last30Days[today])
        assertEquals(1, stats.last30Days[today.minusDays(1)])
        assertEquals(1, stats.last30Days[today.minusDays(5)])
        assertEquals(0, stats.last30Days[today.minusDays(10)])
        // 31 total entries (day 0 through day 30)
        assertEquals(31, stats.last30Days.size)
    }

    @Test
    fun `current streak counts consecutive days including today`() {
        val today = LocalDate.now()
        val events = listOf(
            eventOnDate(today),
            eventOnDate(today.minusDays(1)),
            eventOnDate(today.minusDays(2)),
            eventOnDate(today.minusDays(3))
        )
        val stats = StatisticsCalculator.calculate(events, 4)
        assertEquals(4, stats.currentStreak)
    }

    @Test
    fun `current streak counts from yesterday if no activity today`() {
        val today = LocalDate.now()
        val events = listOf(
            eventOnDate(today.minusDays(1)),
            eventOnDate(today.minusDays(2)),
            eventOnDate(today.minusDays(3))
        )
        val stats = StatisticsCalculator.calculate(events, 3)
        assertEquals(3, stats.currentStreak)
    }

    @Test
    fun `current streak is zero if gap of 2+ days`() {
        val today = LocalDate.now()
        val events = listOf(
            eventOnDate(today.minusDays(3)),
            eventOnDate(today.minusDays(4))
        )
        val stats = StatisticsCalculator.calculate(events, 2)
        assertEquals(0, stats.currentStreak)
    }

    @Test
    fun `longest streak tracks historical maximum`() {
        val today = LocalDate.now()
        // Historical streak of 5
        val events = listOf(
            eventOnDate(today.minusDays(20)),
            eventOnDate(today.minusDays(19)),
            eventOnDate(today.minusDays(18)),
            eventOnDate(today.minusDays(17)),
            eventOnDate(today.minusDays(16)),
            // gap
            eventOnDate(today.minusDays(10)),
            eventOnDate(today.minusDays(9)),
            // gap
            eventOnDate(today)
        )
        val stats = StatisticsCalculator.calculate(events, 8)
        assertEquals(5, stats.longestStreak)
    }

    @Test
    fun `days tracked counts unique days with events`() {
        val today = LocalDate.now()
        val events = listOf(
            eventOnDate(today, hour = 9),
            eventOnDate(today, hour = 14),
            eventOnDate(today.minusDays(1)),
            eventOnDate(today.minusDays(5))
        )
        val stats = StatisticsCalculator.calculate(events, 4)
        assertEquals(3, stats.daysTracked)
    }

    @Test
    fun `single event produces correct statistics`() {
        val today = LocalDate.now()
        val events = listOf(eventOnDate(today))
        val stats = StatisticsCalculator.calculate(events, 1)
        assertEquals(1, stats.totalCount)
        assertEquals(1, stats.currentStreak)
        assertEquals(1, stats.longestStreak)
        assertEquals(1, stats.daysTracked)
    }

    @Test
    fun `decrements do not contribute to streaks`() {
        val today = LocalDate.now()
        val events = listOf(
            eventOnDate(today, delta = -1),
            eventOnDate(today.minusDays(1), delta = -1)
        )
        val stats = StatisticsCalculator.calculate(events, 0)
        assertEquals(0, stats.currentStreak)
    }

    @Test
    fun `monthly trend is sorted chronologically`() {
        val events = listOf(
            eventOnDate(LocalDate.of(2025, 3, 1)),
            eventOnDate(LocalDate.of(2025, 1, 1)),
            eventOnDate(LocalDate.of(2025, 2, 1))
        )
        val stats = StatisticsCalculator.calculate(events, 3)
        val keys = stats.monthlyTrend.keys.toList()
        assertEquals(listOf("2025-01", "2025-02", "2025-03"), keys)
    }
}
