package com.example.a7minutesworkout.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a7minutesworkout.Constants.Companion.DB_NAME
import com.example.a7minutesworkout.ExerciseModel

@Database(entities = [ExerciseModel::class], version = 1, exportSchema = false)
abstract class ExerciseDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao

    companion object {

        private var INSTANCE: ExerciseDatabase? = null

        fun getAppDatabase(context: Context): ExerciseDatabase {

            return INSTANCE ?: synchronized(this) {
                val dbInstance = Room.databaseBuilder(
                    context.applicationContext,
                    ExerciseDatabase::class.java,
                    DB_NAME
                )
                    // .addMigrations(...)
                    .build()
                INSTANCE = dbInstance
                dbInstance
            }
        }
        // return dbInstance ?: throw IllegalStateException("Database not initialized")
    }
}

