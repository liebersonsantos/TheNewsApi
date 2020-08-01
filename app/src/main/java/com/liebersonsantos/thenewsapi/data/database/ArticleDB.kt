package com.liebersonsantos.thenewsapi.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.liebersonsantos.thenewsapi.data.model.Article

@Database(entities = [Article::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ArticleDB : RoomDatabase() {

    abstract fun articleDao(): ArticleDAO

    companion object {
        @Volatile
        private var instance: ArticleDB? = null
        private var LOCK = Any()

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //implement migration
            }

        }

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDB(context).also { instance = it }
        }

        private fun createDB(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ArticleDB::class.java,
            "article_db.db")
            .addMigrations(MIGRATION_1_2)
            .build()
    }
}