package com.example.edu.ui.subject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edu.MainActivity
import com.example.edu.R
import com.example.edu.databinding.FragmentSubjectBinding
import org.json.JSONArray
import org.json.JSONObject
import kotlin.String
import kotlin.getValue

data class SubjectItem(
    val titulo: String,
    val link: String,
    val avaliado: Boolean,
    val resumo_aula: String,
    val topicos_quiz: JSONObject?,
)

class SubjectFragment : Fragment() {
    private val subjectViewModel: SubjectViewModel by activityViewModels()
    private var _binding: FragmentSubjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var subjectAdapter: SubjectsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val subject_text: TextView = binding.subjectText
        val recycler: RecyclerView = binding.recyclerList

        subjectAdapter = SubjectsAdapter { item ->
            (requireActivity() as MainActivity).subjectResumeViewModel.setConfig(item)
            findNavController().navigate(
                R.id.action_navigation_subject_to_subject_resume
            )
        }
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = subjectAdapter


        subjectViewModel.config.observe(viewLifecycleOwner) { json ->
            val subject_name = json.optString("disciplina")
            subject_text.text = subject_name

            val items = mutableListOf<SubjectItem>()

            val modulos = json.optJSONArray("modulos")

            if (modulos != null) {
                for (i in 0 until modulos.length()) {
                    val modulo = modulos.optJSONObject(i) ?: continue

                    items.add(
                        SubjectItem(
                            titulo = modulo.optString("titulo"),
                            link = modulo.optString("link"),
                            avaliado = modulo.optBoolean("avaliado", false),
                            resumo_aula = modulo.optString("resumo_aula"),
                            topicos_quiz = modulo.optJSONObject("topicos_quiz"),
                        )
                    )
                }
            }

            subjectAdapter.submitList(items)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}