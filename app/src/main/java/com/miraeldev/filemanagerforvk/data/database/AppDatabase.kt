package com.miraeldev.filemanagerforvk.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FileDbModel::class], version = 7, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        private const val DB_NAME = "AppDatabase"
        private var db: AppDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context): AppDatabase {
            kotlin.synchronized(lock) {
                db?.let { return it }
                val instance = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                db = instance
                return instance
            }
        }
    }

    abstract fun fileListDao(): FileListDao

}