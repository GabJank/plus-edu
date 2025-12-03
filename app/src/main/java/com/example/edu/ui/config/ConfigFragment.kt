package com.example.edu.ui.config

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.edu.R
import androidx.navigation.fragment.findNavController
import com.example.edu.MainActivity
import com.example.edu.databinding.FragmentConfigBinding
import kotlin.getValue

class ConfigFragment : Fragment() {
    private val configViewModel: ConfigViewModel by activityViewModels()
    private var _binding: FragmentConfigBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val aboutButton: CardView = binding.aboutButton
        val cleanButton: CardView = binding.resetButton

        aboutButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_navigation_config_to_about
            )
        }

        cleanButton.setOnClickListener {
            (requireActivity() as MainActivity).resetConfigJson()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}