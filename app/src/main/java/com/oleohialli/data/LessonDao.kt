package com.oleohialli.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LessonDao {
    @Query("SELECT * FROM lesson_table")
    fun getLessons(): LiveData<List<Lesson>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLesson(lesson: Lesson)
}