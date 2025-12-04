package com.example.edu.ui.subject

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.edu.R
import androidx.navigation.fragment.findNavController
import com.example.edu.databinding.FragmentSubjectFinishedBinding
import android.os.Handler
import kotlin.getValue

class SubjectFinishedFragment : Fragment() {
    private val subjectFinishedViewModel: SubjectFinishedViewModel by activityViewModels()
    private var _binding: FragmentSubjectFinishedBinding? = null
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val confirm_button: CardView = binding.confirmButton

        confirm_button.setOnClickListener {
            voltarParaHome()
        }

        // Auto-retorno depois de 5 segundos
        handler.postDelayed({
            voltarParaHome()
        }, 5000L)

        return root
    }

    private fun voltarParaHome() {
        findNavController().popBackStack(
            R.id.navigation_home,
            false
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        _binding = null
    }

}