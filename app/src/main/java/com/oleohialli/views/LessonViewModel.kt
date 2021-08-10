package com.oleohialli.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.oleohialli.api.LessonRepository
import com.oleohialli.data.Lesson
import com.oleohialli.data.LessonDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val lessonDao: LessonDao,
    private val repository: LessonRepository
) : ViewModel() {

    lateinit var startTime: String
    lateinit var endTime: String

    private val lessonEventChannel = Channel<LessonEvent>()
    val lessonEvent = lessonEventChannel.receiveAsFlow()

    fun getLessons() = liveData(Dispatchers.IO) {
        emit(repository.getLessons())
    }

    fun saveLesson(lesson: Lesson) {
        viewModelScope.launch {
            val lessonModel = Lesson(lesson.id, startTime = startTime, endTime = endTime)
            lessonDao.saveLesson(lessonModel)
        }
    }


    fun showLessonCompleteMessage(msg: String) = viewModelScope.launch {
        lessonEventChannel.send(LessonEvent.ShowLessonSavedMessage(msg))
    }

    sealed class LessonEvent {
        data class ShowNetworkError(val message: String) : LessonEvent()
        object ShowNextLesson : LessonEvent()
        data class ShowLessonSavedMessage(val message: String) : LessonEvent()
    }
}