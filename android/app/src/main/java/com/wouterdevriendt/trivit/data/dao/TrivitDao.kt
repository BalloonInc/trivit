package com.wouterdevriendt.trivit.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wouterdevriendt.trivit.data.model.Trivit
import kotlinx.coroutines.flow.Flow

@Dao
interface TrivitDao {

    @Query("SELECT * FROM trivits WHERE deletedAt IS NULL ORDER BY sortOrder ASC")
    fun getAllActive(): Flow<List<Trivit>>

    @Query("SELECT * FROM trivits WHERE deletedAt IS NOT NULL ORDER BY deletedAt DESC")
    fun getAllDeleted(): Flow<List<Trivit>>

    @Query("SELECT * FROM trivits WHERE id = :id")
    suspend fun getById(id: Long): Trivit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trivit: Trivit): Long

    @Update
    suspend fun update(trivit: Trivit)

    @Delete
    suspend fun delete(trivit: Trivit)

    @Query("UPDATE trivits SET count = count + :delta WHERE id = :id")
    suspend fun incrementCount(id: Long, delta: Int = 1)

    @Query("UPDATE trivits SET count = 0 WHERE id = :id")
    suspend fun resetCount(id: Long)

    @Query("UPDATE trivits SET name = :name WHERE id = :id")
    suspend fun rename(id: Long, name: String)

    @Query("UPDATE trivits SET colorIndex = :colorIndex WHERE id = :id")
    suspend fun updateColor(id: Long, colorIndex: Int)

    @Query("UPDATE trivits SET isExpanded = :expanded WHERE id = :id")
    suspend fun setExpanded(id: Long, expanded: Boolean)

    @Query("UPDATE trivits SET sortOrder = :sortOrder WHERE id = :id")
    suspend fun updateSortOrder(id: Long, sortOrder: Int)

    @Query("UPDATE trivits SET deletedAt = :deletedAt WHERE id = :id")
    suspend fun softDelete(id: Long, deletedAt: Long = System.currentTimeMillis())

    @Query("UPDATE trivits SET deletedAt = NULL WHERE id = :id")
    suspend fun restore(id: Long)

    @Query("DELETE FROM trivits WHERE deletedAt IS NOT NULL AND deletedAt < :cutoff")
    suspend fun purgeOldDeleted(cutoff: Long)

    @Query("SELECT MAX(sortOrder) FROM trivits WHERE deletedAt IS NULL")
    suspend fun getMaxSortOrder(): Int?

    @Query("SELECT COUNT(*) FROM trivits WHERE deletedAt IS NULL")
    suspend fun getActiveCount(): Int
}
