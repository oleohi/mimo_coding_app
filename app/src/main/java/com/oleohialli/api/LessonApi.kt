package com.oleohialli.api

import retrofit2.http.GET

interface LessonApi {

    companion object {
        const val BASE_URL = "https://mimochallenge.azurewebsites.net/api/"
    }

    @GET("lessons")
    suspend fun getLessons(): LessonResponse
}