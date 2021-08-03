package com.oleohialli.views

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.oleohialli.R
import com.oleohialli.databinding.FragmentLessonBinding
import com.oleohialli.models.Lesson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import okhttp3.internal.immutableListOf
import okhttp3.internal.toImmutableList

@AndroidEntryPoint
class LessonFragment : Fragment(R.layout.fragment_lesson) {

    private val TAG = "MimoApp"
    private val viewModel by viewModels<LessonViewModel>()

    private var _binding: FragmentLessonBinding? = null
    private val binding get() = _binding!!
    private var lessons = immutableListOf<Lesson>()
    private var lessonIndex = 0
    private var contentString = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLessonBinding.bind(view)

        getLessons()

        //Todo: Use this block to collect interaction events
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.lessonEvent.collect { event ->
                when (event) {
                    is LessonViewModel.LessonEvent.ShowNetworkError -> {
                        Snackbar.make(
                            requireView(),
                            "Error retrieving lessons",
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("RETRY") {
                                getLessons()
                            }.show()
                    }
                }
            }

        }

        binding.nextButton.setOnClickListener {

            //clear text views
            refreshFragment(binding.contentLayout)
            lessonIndex++
            contentString = ""

            // Load next lesson
            if (lessonIndex < lessons.size) {
                val lesson = loadLesson(lessonIndex)
                lesson.content.forEach {
                    inflateTextView(binding.contentLayout, it.text, it.color)
                    contentString += it.text
                }
                println(contentString)
                if (lesson.input != null) {
                    // Create substring and load input field
                }

            } else {
                binding.apply {
                    textViewDone.isVisible = true
                    nextButton.isVisible = false
                    contentLayout.isVisible = false
                }
            }
        }
    }

    private fun getLessons() {
        binding.apply {
            progressLoader.isVisible = true
            nextButton.isVisible = false
        }

        viewModel.getLessons().observe(viewLifecycleOwner, { result ->
            if (result.isSuccess) {
                result.onSuccess { it ->
                    binding.progressLoader.isVisible = false
                    binding.nextButton.isVisible = true
                    lessons = it
                    // Load first lesson (which is at index 0)
                    val lesson = loadLesson(lessonIndex)
                    lesson.content.forEach {
                        inflateTextView(binding.contentLayout, it.text, it.color)
                        contentString += it.text
                    }
                    println(contentString)
                    if (lesson.input != null) {
                        // Create substring and load input field
                    }

                }
            } else {
                result.onFailure {
                    binding.apply {
                        progressLoader.isVisible = false
                        binding.nextButton.isVisible = false
                    }
                    Snackbar.make(
                        requireView(),
                        "Error retrieving lessons",
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction("RETRY") {
                            getLessons()
                        }.show()
                }
            }

        })
    }

    private fun loadLesson(index: Int): Lesson {
        return lessons[index]
    }

    private fun inflateTextView(contentLayout: LinearLayout, text: String, color: String) {
        val textView = TextView(requireContext())
        textView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
        textView.setPadding(8, 8, 8, 8)
        textView.text = text
        textView.textSize = 16F
        textView.setTextColor(Color.parseColor(color))
        contentLayout.addView(textView)
    }

    private fun inflateInputField(contentLayout: LinearLayout, startIndex: Int, endIndex: Int) {
        val editText = EditText(requireContext())
        editText.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        editText.setPadding(8, 8, 8, 8)
        contentLayout.addView(editText)
    }

    private fun refreshFragment(contentLayout: LinearLayout) {
        contentLayout.children.forEach {
            if (it is TextView) {
                it.text = ""
            }
        }
    }
}