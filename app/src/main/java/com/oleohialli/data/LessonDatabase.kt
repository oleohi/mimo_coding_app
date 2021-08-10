package com.oleohialli.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.oleohialli.Constants
import javax.inject.Inject

@Database(entities = [Lesson::class], version = Constants.DATABASE_VERSION_NUMBER)
abstract class LessonDatabase : RoomDatabase() {

    abstract fun lessonDao(): LessonDao

    class Callback @Inject constructor(
    ) : RoomDatabase.Callback()
}