package com.example.edu.ui.subject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.edu.databinding.FragmentAboutBinding
import com.example.edu.databinding.FragmentSubjectBinding
import com.example.edu.databinding.FragmentSubjectResumeBinding
import kotlin.getValue

class SubjectResumeFragment : Fragment() {
    private val subjectResumeViewModel: SubjectResumeViewModel by activityViewModels()
    private var _binding: FragmentSubjectResumeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectResumeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}