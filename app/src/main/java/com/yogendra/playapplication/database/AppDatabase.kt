package com.yogendra.playapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yogendra.playapplication.DATABASE_NAME
import com.yogendra.playapplication.data.Allkeys
import com.yogendra.playapplication.data.Converters
import com.yogendra.playapplication.data.Itemdetail
import com.yogendra.playapplication.datasource.local.AllKeysDao
import com.yogendra.playapplication.datasource.local.ItemDetailsDao


/**
 * The Room database for this app
 */
@Database(
    entities = [Itemdetail::class,
        Allkeys::class],
    version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun keysDao(): AllKeysDao

    abstract fun detailsDao(): ItemDetailsDao


    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                })
                .build()
        }
    }
}
