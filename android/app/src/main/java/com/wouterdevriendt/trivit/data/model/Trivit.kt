package com.wouterdevriendt.trivit.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trivits")
data class Trivit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val count: Int = 0,
    val colorIndex: Int = 0,
    val sortOrder: Int = 0,
    val isExpanded: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
)
