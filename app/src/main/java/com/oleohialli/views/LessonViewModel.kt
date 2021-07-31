package com.oleohialli.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.oleohialli.models.LessonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    repository: LessonRepository) : ViewModel() {

    val lessons = repository.getLessons().cachedIn(viewModelScope)
}