package com.kierdavis.fuchsia.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kierdavis.fuchsia.model.Item
import com.kierdavis.fuchsia.model.ItemPicture
import com.kierdavis.fuchsia.database.converters.UriConverters

@Database(entities = [Item::class, ItemPicture::class], version = 1)
@TypeConverters(UriConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun itemPictureDao(): ItemPictureDao

    companion object AppDatabaseProvider {
        @Volatile
        private var theInstance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return theInstance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "fuchsia").build().also {
                    theInstance = it
                }
            }
        }
    }
}