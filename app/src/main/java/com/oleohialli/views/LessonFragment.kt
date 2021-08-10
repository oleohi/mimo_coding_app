package com.oleohialli.views

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.oleohialli.R
import com.oleohialli.databinding.FragmentLessonBinding
import com.oleohialli.data.Lesson
import com.oleohialli.data.Lesson.LessonContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import okhttp3.internal.immutableListOf
import java.sql.Time
import java.util.*

@AndroidEntryPoint
class LessonFragment : Fragment(R.layout.fragment_lesson) {

    private val TAG = "MimoApp"
    private val viewModel by viewModels<LessonViewModel>()

    private var _binding: FragmentLessonBinding? = null
    private val binding get() = _binding!!
    private var lessons = immutableListOf<Lesson>()
    private var lessonIndex = 0
    private var contentString: String = ""
    private var interactionString: String = ""
    private var startIndex: Int = 0
    private var endIndex: Int = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLessonBinding.bind(view)

        getLessons()

        // Use this block to collect interaction events
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.lessonEvent.collect { event ->
                when (event) {
                    is LessonViewModel.LessonEvent.ShowLessonSavedMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.nextButton.setOnClickListener {
            clearContent(binding.contentLayout)
            contentString = ""
            displayLesson()
        }
    }

    private fun getLessons() {
        binding.apply {
            progressLoader.isVisible = true
            nextButton.isVisible = false
        }

        viewModel.getLessons().observe(viewLifecycleOwner, { result ->
            if (result.isSuccess) {
                result.onSuccess {
                    binding.progressLoader.isVisible = false
                    binding.nextButton.isVisible = true

                    lessons = it
                    displayLesson()
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

    private fun displayLesson() {

        if (lessonIndex < lessons.size) {
            viewModel.startTime = Calendar.getInstance().time.toString()

            val lesson = lessons[lessonIndex]
            lesson.content.forEach {
                contentString += it.text
            }

            if (lesson.input != null) {
                val input = lesson.input
                interactionString = contentString.substring(input!!.startIndex, input.endIndex)
                startIndex = input.startIndex
                endIndex = input.endIndex

                // Because there are three possible ways the input display can come in
                // [beginning, middle and ending] of the concatenated string of content.
                // This is not the most efficient way to do this.
                // Ideally, the views should be populated dynamically
                if (startIndex == 0) {
                    inflateEditText(binding.contentLayout, lesson.content[0].color, lesson)
                    inflateTextView(binding.contentLayout, lesson.content[1].text, lesson.content[1].color)
                    inflateTextView(binding.contentLayout, lesson.content[2].text, lesson.content[2].color)
                }
                if(startIndex > 0 && endIndex < contentString.length - 1){
                    inflateTextView(binding.contentLayout, lesson.content[0].text, lesson.content[0].color)
                    inflateEditText(binding.contentLayout, lesson.content[1].color, lesson)
                    inflateTextView(binding.contentLayout, lesson.content[2].text, lesson.content[2].color)
                }
                if(endIndex == contentString.length - 1) {
                    inflateTextView(binding.contentLayout, lesson.content[0].text, lesson.content[0].color)
                    inflateTextView(binding.contentLayout, lesson.content[1].text, lesson.content[1].color)
                    inflateEditText(binding.contentLayout, lesson.content[2].color, lesson)
                }

            } else {
                interactionString = ""
                lesson.content.forEach {
                    inflateTextView(binding.contentLayout, it.text, it.color)
                }
            }

        } else {
            binding.apply {
                textViewDone.isVisible = true
                nextButton.isVisible = false
                contentLayout.isVisible = false
            }
        }
        lessonIndex++
    }

    private fun saveLesson(lesson: Lesson) {
        viewModel.endTime = Calendar.getInstance().time.toString()
        viewModel.saveLesson(lesson)
        Log.v(TAG, "SAVED!!")
        viewModel.showLessonCompleteMessage("Lesson saved!")
    }

    private fun inflateEditText(
        contentLayout: LinearLayout,
        color: String,
        lesson: Lesson
    ) {

        val editText = EditText(requireContext())
        //add edit text properties
        editText.layoutParams = LinearLayout.LayoutParams(
            200, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        editText.setPadding(8, 8, 8, 8)
        editText.setTextColor(Color.parseColor(color))
        //editText.maxWidth = inputLength
        editText.maxLines = 1
        editText.isFocusable = true
        editText.requestFocus()
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.nextButton.isVisible = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                if (interactionString == s.toString()) {
                    binding.nextButton.isVisible = true
                    saveLesson(lesson)
                }
            }
        })
        contentLayout.addView(editText)
    }

    private fun inflateTextView(contentLayout: LinearLayout, text: String, color: String) {
        val textView = TextView(requireContext())
        //add text view properties
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

    private fun clearContent(contentLayout: LinearLayout) {
        contentLayout.children.forEach {
            if (it is TextView) {
                it.text = ""
            }
        }
    }
}