package com.kierdavis.fuchsia.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kierdavis.fuchsia.model.Item
import com.kierdavis.fuchsia.model.ItemPicture
import com.kierdavis.fuchsia.database.converters.UriConverters
import com.kierdavis.fuchsia.model.Collection
import com.kierdavis.fuchsia.model.CollectionItem

@Database(entities = [Item::class, ItemPicture::class, Collection::class, CollectionItem::class], version = 3)
@TypeConverters(UriConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun itemPictureDao(): ItemPictureDao
    abstract fun collectionDao(): CollectionDao

    companion object {
        @Volatile
        private var theInstance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return theInstance ?: synchronized(this) {
                newInstance(context).also { theInstance = it }
            }
        }

        private fun newInstance(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "fuchsia").addMigrations(
                object : Migration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("CREATE TABLE Collection (id INTEGER NOT NULL, name TEXT NOT NULL, PRIMARY KEY(id))")
                    }
                },
                object : Migration(2, 3) {
                    override fun migrate(database: SupportSQLiteDatabase) {
                        database.execSQL("CREATE TABLE CollectionItem (collectionId INTEGER NOT NULL, itemId INTEGER NOT NULL, PRIMARY KEY(collectionId, itemId))")
                    }
                }
            ).build()
    }
}