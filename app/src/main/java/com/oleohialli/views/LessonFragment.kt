package com.oleohialli.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.oleohialli.R
import com.oleohialli.databinding.FragmentLessonBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LessonFragment : Fragment(R.layout.fragment_lesson) {

    private val viewModel by viewModels<LessonViewModel>()

    private var _binding: FragmentLessonBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLessonBinding.bind(view)

        viewModel.lessons.observe(viewLifecycleOwner) {

        }
    }
}