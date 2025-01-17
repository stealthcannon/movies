package org.michaelbel.movies.persistence.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.michaelbel.movies.entities.BuildConfig
import org.michaelbel.movies.persistence.database.converter.CalendarConverter
import org.michaelbel.movies.persistence.database.dao.AccountDao
import org.michaelbel.movies.persistence.database.dao.MovieDao
import org.michaelbel.movies.persistence.database.dao.PagingKeyDao
import org.michaelbel.movies.persistence.database.entity.AccountDb
import org.michaelbel.movies.persistence.database.entity.MovieDb
import org.michaelbel.movies.persistence.database.entity.PagingKeyDb

/**
 * The Room database for this app
 */
@Database(
    entities = [
        MovieDb::class,
        AccountDb::class,
        PagingKeyDb::class
    ],
    version = AppDatabase.DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(CalendarConverter::class)
internal abstract class AppDatabase: RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun accountDao(): AccountDao
    abstract fun pagingKeyDao(): PagingKeyDao

    companion object {
        private val DATABASE_NAME: String = if (BuildConfig.DEBUG) "movies-db-debug" else "movies-db"
        const val DATABASE_VERSION = 14

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}