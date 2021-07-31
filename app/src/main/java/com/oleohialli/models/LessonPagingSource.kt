package com.oleohialli.models

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.oleohialli.api.LessonsApi
import retrofit2.HttpException
import java.io.IOException


private const val LESSON_STARTING_PAGE_INDEX = 1

class LessonPagingSource(
    private val lessonsApi: LessonsApi
) : PagingSource<Int, Lesson>() {

    override fun getRefreshKey(state: PagingState<Int, Lesson>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Lesson> {
        val  position = params.key ?: LESSON_STARTING_PAGE_INDEX

        return try {
            val response = lessonsApi.getLessons()
            val lessons = response.result.lessons

            LoadResult.Page(
                data = lessons,
                prevKey = if (position == LESSON_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (lessons.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

}