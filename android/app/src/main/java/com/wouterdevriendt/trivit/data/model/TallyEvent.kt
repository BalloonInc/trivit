package com.wouterdevriendt.trivit.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tally_events",
    foreignKeys = [
        ForeignKey(
            entity = Trivit::class,
            parentColumns = ["id"],
            childColumns = ["trivitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("trivitId")]
)
data class TallyEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val trivitId: Long,
    val delta: Int = 1,
    val timestamp: Long = System.currentTimeMillis()
)
