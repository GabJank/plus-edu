package com.example.edu.ui.subject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.edu.databinding.FragmentSubjectBinding
import kotlin.getValue

class SubjectFragment : Fragment() {
    private val subjectViewModel: SubjectViewModel by activityViewModels()
    private var _binding: FragmentSubjectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val subject_text: TextView = binding.subjectText

        subjectViewModel.config.observe(viewLifecycleOwner) { json ->
            Log.d("+EDU", "$json")
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}