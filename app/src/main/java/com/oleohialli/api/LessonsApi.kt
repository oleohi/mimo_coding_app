package com.oleohialli.api

import retrofit2.http.GET

interface LessonsApi {

    companion object {
        const val BASE_URL = "https://mimochallenge.azurewebsites.net/api/"
    }

    @GET("lessons")
    suspend fun getLessons(): LessonsResponse
}