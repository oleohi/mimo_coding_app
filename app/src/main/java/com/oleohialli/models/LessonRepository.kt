package com.oleohialli.models

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.oleohialli.api.LessonsApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonRepository @Inject constructor(private val lessonsApi: LessonsApi) {

    fun getLessons() = Pager(
        config = PagingConfig(
            pageSize = 3,
            maxSize = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { LessonPagingSource(lessonsApi) }
    ).liveData
}