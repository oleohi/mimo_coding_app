package com.oleohialli.data

//@Entity(tableName = Constants.TABLE_NAME)
data class Lesson(
    val id: Int,
    val content: List<LessonContent>,
    val input: InputParams
)  {

    data class LessonContent(
        val color: String,
        val text: String
    )

    data class InputParams(
        val startIndex: Int,
        val endIndex: Int
    )

}

