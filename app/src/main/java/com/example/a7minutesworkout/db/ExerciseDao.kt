package com.example.a7minutesworkout.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.a7minutesworkout.ExerciseModel
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.NotNull

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExerciseData(exerciseModel: List<ExerciseModel>)

    @Delete
    suspend fun deleteExercise(exerciseModel: ExerciseModel)

    @Update
    suspend fun updateExercise(exerciseModel: ExerciseModel)

    @Query(" SELECT * FROM SevenMinExercise_Table ORDER BY ID ASC")
    fun getExerciseAllData(): Flow<List<ExerciseModel>>
}