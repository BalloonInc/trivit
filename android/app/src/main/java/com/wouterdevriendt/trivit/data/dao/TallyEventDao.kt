package com.wouterdevriendt.trivit.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.wouterdevriendt.trivit.data.model.TallyEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface TallyEventDao {

    @Query("SELECT * FROM tally_events WHERE trivitId = :trivitId ORDER BY timestamp DESC")
    fun getEventsForTrivit(trivitId: Long): Flow<List<TallyEvent>>

    @Query("SELECT * FROM tally_events WHERE trivitId = :trivitId ORDER BY timestamp DESC")
    suspend fun getEventsForTrivitOnce(trivitId: Long): List<TallyEvent>

    @Insert
    suspend fun insert(event: TallyEvent): Long

    @Delete
    suspend fun delete(event: TallyEvent)

    @Query("DELETE FROM tally_events WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM tally_events WHERE trivitId = :trivitId")
    suspend fun deleteAllForTrivit(trivitId: Long)

    @Query("SELECT COUNT(*) FROM tally_events WHERE trivitId = :trivitId")
    suspend fun getEventCount(trivitId: Long): Int
}
