package com.oleohialli.data

import androidx.room.*
import com.oleohialli.Constants

@Entity(tableName = Constants.TABLE_NAME)
data class Lesson(
    @PrimaryKey
    var id: Int = 1,

    @Ignore
    val content: ArrayList<LessonContent> = arrayListOf(),

    @Ignore
    var input: InputParams? = null,

    var startTime: String = "",
    var endTime: String = ""
    ) {

    data class LessonContent(
        val color: String,
        val text: String
    )

    data class InputParams(
        val startIndex: Int,
        val endIndex: Int
    )

}
