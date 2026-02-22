package com.wouterdevriendt.trivit.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wouterdevriendt.trivit.data.dao.TallyEventDao
import com.wouterdevriendt.trivit.data.dao.TrivitDao
import com.wouterdevriendt.trivit.data.model.TallyEvent
import com.wouterdevriendt.trivit.data.model.Trivit

@Database(
    entities = [Trivit::class, TallyEvent::class],
    version = 1,
    exportSchema = false
)
abstract class TrivitDatabase : RoomDatabase() {
    abstract fun trivitDao(): TrivitDao
    abstract fun tallyEventDao(): TallyEventDao
}
