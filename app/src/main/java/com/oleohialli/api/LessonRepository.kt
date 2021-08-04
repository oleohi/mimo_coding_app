package com.oleohialli.api

import com.oleohialli.data.Lesson
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonRepository @Inject constructor(private val lessonsApi: LessonApi) {

    suspend fun getLessons() : Result<List<Lesson>> {
        return try {
            val response = lessonsApi.getLessons()
            Result.success(response.lessons)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}