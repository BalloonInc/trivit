package com.wouterdevriendt.trivit.data.repository

import com.wouterdevriendt.trivit.data.dao.TallyEventDao
import com.wouterdevriendt.trivit.data.dao.TrivitDao
import com.wouterdevriendt.trivit.data.model.TallyEvent
import com.wouterdevriendt.trivit.data.model.Trivit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrivitRepository @Inject constructor(
    private val trivitDao: TrivitDao,
    private val tallyEventDao: TallyEventDao
) {
    fun getAllActive(): Flow<List<Trivit>> = trivitDao.getAllActive()

    fun getAllDeleted(): Flow<List<Trivit>> = trivitDao.getAllDeleted()

    fun getEventsForTrivit(trivitId: Long): Flow<List<TallyEvent>> =
        tallyEventDao.getEventsForTrivit(trivitId)

    suspend fun getEventsForTrivitOnce(trivitId: Long): List<TallyEvent> =
        tallyEventDao.getEventsForTrivitOnce(trivitId)

    suspend fun getTrivitById(id: Long): Trivit? = trivitDao.getById(id)

    suspend fun createTrivit(trivit: Trivit): Long {
        val maxOrder = trivitDao.getMaxSortOrder() ?: -1
        return trivitDao.insert(trivit.copy(sortOrder = maxOrder + 1))
    }

    suspend fun updateTrivit(trivit: Trivit) = trivitDao.update(trivit)

    suspend fun increment(trivitId: Long, delta: Int = 1) {
        trivitDao.incrementCount(trivitId, delta)
        tallyEventDao.insert(TallyEvent(trivitId = trivitId, delta = delta))
    }

    suspend fun decrement(trivitId: Long) {
        val trivit = trivitDao.getById(trivitId) ?: return
        if (trivit.count > 0) {
            trivitDao.incrementCount(trivitId, -1)
            tallyEventDao.insert(TallyEvent(trivitId = trivitId, delta = -1))
        }
    }

    suspend fun resetCount(trivitId: Long) {
        trivitDao.resetCount(trivitId)
    }

    suspend fun rename(trivitId: Long, name: String) {
        trivitDao.rename(trivitId, name)
    }

    suspend fun updateColor(trivitId: Long, colorIndex: Int) {
        trivitDao.updateColor(trivitId, colorIndex)
    }

    suspend fun setExpanded(trivitId: Long, expanded: Boolean) {
        trivitDao.setExpanded(trivitId, expanded)
    }

    suspend fun softDelete(trivitId: Long) {
        trivitDao.softDelete(trivitId)
    }

    suspend fun restore(trivitId: Long) {
        trivitDao.restore(trivitId)
    }

    suspend fun permanentlyDelete(trivit: Trivit) {
        trivitDao.delete(trivit)
    }

    suspend fun purgeOldDeleted(olderThanDays: Int = 30) {
        val cutoff = System.currentTimeMillis() - (olderThanDays.toLong() * 24 * 60 * 60 * 1000)
        trivitDao.purgeOldDeleted(cutoff)
    }

    suspend fun updateSortOrders(trivits: List<Trivit>) {
        trivits.forEachIndexed { index, trivit ->
            trivitDao.updateSortOrder(trivit.id, index)
        }
    }

    suspend fun deleteEvent(event: TallyEvent) {
        tallyEventDao.delete(event)
        // Adjust the trivit count
        trivitDao.incrementCount(event.trivitId, -event.delta)
    }

    suspend fun deleteEventById(eventId: Long, trivitId: Long, delta: Int) {
        tallyEventDao.deleteById(eventId)
        trivitDao.incrementCount(trivitId, -delta)
    }

    suspend fun getMaxSortOrder(): Int = trivitDao.getMaxSortOrder() ?: -1
}
