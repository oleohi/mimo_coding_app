package com.oleohialli.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lesson(
    val id: Int,
    val content: List<LessonContent>,
    val input: InputParams
) : Parcelable {

    @Parcelize
    data class LessonContent(
        val contentData: List<ContentData>
    ) : Parcelable {

        @Parcelize
        data class ContentData(
            val color: String,
            val text: String
        ) : Parcelable
    }

    @Parcelize
    data class InputParams(
        val startIndex: Int,
        val endIndex: Int
    ) : Parcelable

}
