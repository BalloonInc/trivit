package com.wouterdevriendt.trivit.domain

import com.wouterdevriendt.trivit.data.model.TallyEvent
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

data class TrivitStatistics(
    val totalCount: Int,
    val currentStreak: Int,
    val longestStreak: Int,
    val daysTracked: Int,
    val hourlyDistribution: Map<Int, Int>,
    val dayOfWeekDistribution: Map<Int, Int>,
    val monthlyTrend: Map<String, Int>,
    val last30Days: Map<LocalDate, Int>
)

object StatisticsCalculator {

    fun calculate(events: List<TallyEvent>, currentCount: Int): TrivitStatistics {
        if (events.isEmpty()) {
            return TrivitStatistics(
                totalCount = currentCount,
                currentStreak = 0,
                longestStreak = 0,
                daysTracked = 0,
                hourlyDistribution = emptyMap(),
                dayOfWeekDistribution = emptyMap(),
                monthlyTrend = emptyMap(),
                last30Days = emptyMap()
            )
        }

        val zone = ZoneId.systemDefault()
        val dateTimes = events.map {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it.timestamp), zone)
        }
        val dates = dateTimes.map { it.toLocalDate() }.distinct().sorted()

        // Hourly distribution (0-23)
        val hourly = dateTimes
            .filter { events[dateTimes.indexOf(it)].delta > 0 }
            .groupingBy { it.hour }
            .eachCount()

        // Day of week distribution (1=Monday, 7=Sunday)
        val dayOfWeek = dateTimes
            .filter { events[dateTimes.indexOf(it)].delta > 0 }
            .groupingBy { it.dayOfWeek.value }
            .eachCount()

        // Monthly trend
        val monthly = dateTimes
            .filter { events[dateTimes.indexOf(it)].delta > 0 }
            .groupingBy { "${it.year}-${it.monthValue.toString().padStart(2, '0')}" }
            .eachCount()
            .toSortedMap()

        // Last 30 days
        val thirtyDaysAgo = LocalDate.now().minusDays(30)
        val last30 = mutableMapOf<LocalDate, Int>()
        for (i in 0L..30L) {
            last30[thirtyDaysAgo.plusDays(i)] = 0
        }
        events.filter { it.delta > 0 }.forEach { event ->
            val date = Instant.ofEpochMilli(event.timestamp).atZone(zone).toLocalDate()
            if (!date.isBefore(thirtyDaysAgo)) {
                last30[date] = (last30[date] ?: 0) + event.delta
            }
        }

        // Streak calculation
        val activeDates = events
            .filter { it.delta > 0 }
            .map { Instant.ofEpochMilli(it.timestamp).atZone(zone).toLocalDate() }
            .distinct()
            .sortedDescending()

        val currentStreak = calculateCurrentStreak(activeDates)
        val longestStreak = calculateLongestStreak(activeDates.sortedDescending())

        return TrivitStatistics(
            totalCount = currentCount,
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            daysTracked = dates.size,
            hourlyDistribution = hourly,
            dayOfWeekDistribution = dayOfWeek,
            monthlyTrend = monthly,
            last30Days = last30.toSortedMap()
        )
    }

    private fun calculateCurrentStreak(datesDescending: List<LocalDate>): Int {
        if (datesDescending.isEmpty()) return 0

        val today = LocalDate.now()
        val sorted = datesDescending.sorted().reversed() // most recent first

        // Streak must include today or yesterday
        if (sorted.first() != today && sorted.first() != today.minusDays(1)) {
            return 0
        }

        var streak = 1
        for (i in 0 until sorted.size - 1) {
            if (ChronoUnit.DAYS.between(sorted[i + 1], sorted[i]) == 1L) {
                streak++
            } else {
                break
            }
        }
        return streak
    }

    private fun calculateLongestStreak(datesDescending: List<LocalDate>): Int {
        if (datesDescending.isEmpty()) return 0

        val sorted = datesDescending.sorted() // ascending
        var longest = 1
        var current = 1

        for (i in 1 until sorted.size) {
            if (ChronoUnit.DAYS.between(sorted[i - 1], sorted[i]) == 1L) {
                current++
                longest = maxOf(longest, current)
            } else if (sorted[i] != sorted[i - 1]) {
                current = 1
            }
        }
        return longest
    }
}
