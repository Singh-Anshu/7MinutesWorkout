package com.example.a7minutesworkout.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a7minutesworkout.Constants.Companion.DB_NAME
import com.example.a7minutesworkout.ExerciseModel

@Database(entities = [ExerciseModel::class], version = 1)
abstract class ExerciseDatabase: RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao

    var dbInstance: ExerciseDatabase? = null

    fun getAppDatabase(context: Context): ExerciseDatabase {
        if (dbInstance == null) {
            synchronized(this) {
                dbInstance = Room.databaseBuilder(
                  context.applicationContext,
                    ExerciseDatabase::class.java,
                    DB_NAME
                )
                    // .addMigrations(...)
                    .build()
            }
        }
        return dbInstance ?: throw IllegalStateException("Database not initialized")
    }

}