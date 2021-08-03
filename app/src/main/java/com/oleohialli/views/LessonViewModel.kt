package com.oleohialli.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.oleohialli.models.LessonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val repository: LessonRepository
) : ViewModel() {

    private val lessonEventChannel = Channel<LessonEvent>()
    val lessonEvent = lessonEventChannel.receiveAsFlow()

    fun getLessons() = liveData(Dispatchers.IO) {
        emit(repository.getLessons())
    }


    sealed class LessonEvent {
        data class ShowNetworkError(val message: String) : LessonEvent()
        object ShowNextLesson : LessonEvent()
        data class ShowLessonComplete(val message: String) : LessonEvent()
    }
}