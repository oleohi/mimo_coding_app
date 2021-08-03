package com.oleohialli.models

import com.oleohialli.api.LessonsApi
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonRepository @Inject constructor(private val lessonsApi: LessonsApi) {

    suspend fun getLessons() : Result<List<Lesson>> {
        return try {
            val response = lessonsApi.getLessons()
            Result.success(response.lessons)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}