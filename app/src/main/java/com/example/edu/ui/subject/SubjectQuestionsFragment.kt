package com.example.edu.ui.subject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.edu.R
import androidx.navigation.fragment.findNavController
import com.example.edu.databinding.FragmentConfigBinding
import com.example.edu.databinding.FragmentSubjectBinding
import com.example.edu.databinding.FragmentSubjectQuestionsBinding
import kotlin.getValue

class SubjectQuestionsFragment : Fragment() {
    private val subjectQuestionsViewModel: SubjectQuestionsViewModel by activityViewModels()
    private var _binding: FragmentSubjectQuestionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectQuestionsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}