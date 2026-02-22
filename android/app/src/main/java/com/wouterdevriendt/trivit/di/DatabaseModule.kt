package com.wouterdevriendt.trivit.di

import android.content.Context
import androidx.room.Room
import com.wouterdevriendt.trivit.data.dao.TallyEventDao
import com.wouterdevriendt.trivit.data.dao.TrivitDao
import com.wouterdevriendt.trivit.data.database.TrivitDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TrivitDatabase {
        return Room.databaseBuilder(
            context,
            TrivitDatabase::class.java,
            "trivit_database"
        ).build()
    }

    @Provides
    fun provideTrivitDao(database: TrivitDatabase): TrivitDao {
        return database.trivitDao()
    }

    @Provides
    fun provideTallyEventDao(database: TrivitDatabase): TallyEventDao {
        return database.tallyEventDao()
    }
}
