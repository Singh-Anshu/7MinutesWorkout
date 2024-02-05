package com.example.a7minutesworkout

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SevenMinExercise_Table")
data class ExerciseModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    private var id: Int,

    @ColumnInfo(name = "ExerciseName")
    private var name: String,

    @ColumnInfo(name = "Image")
    private var image: Int,

    @ColumnInfo(name = "IsCompleted")
    private var isCompleted: Boolean,

    @ColumnInfo(name = "IsSelected")
    private var isSelected: Boolean
)

/*
{

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getImage(): Int {
        return image
    }

    fun setImage(image: Int) {
        this.image = image
    }

    fun getIsCompleted(): Boolean {
        return isCompleted
    }

    fun setIsCompleted(isCompleted: Boolean) {
        this.isCompleted = isCompleted
    }

    fun getIsSelected(): Boolean {
        return isSelected
    }

    fun setIsSelected(isSelected: Boolean) {
        this.isSelected = isSelected
    }
}*/
