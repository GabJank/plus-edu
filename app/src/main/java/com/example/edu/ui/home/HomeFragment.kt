package com.example.edu.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.edu.MainActivity
import androidx.navigation.fragment.findNavController
import com.example.edu.R
import com.example.edu.databinding.FragmentHomeBinding
import com.example.edu.ui.subject.SubjectViewModel

data class RankingItem(
    val posicao: Int,
    val nome: String,
    val pontos: Int,
    val iconRes: Int
)

class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val nameView: TextView = binding.nameHome
        val idadeView: TextView = binding.idadeHome
        val formacaoView: TextView = binding.formacaoHome
        val disciplinaView: TextView = binding.materiaNomeHome
        val icon: ImageView = binding.iconImage
        val mathButton: CardView = binding.math
        val portugueseButton: CardView = binding.portuguese
        val scienceButton: CardView = binding.science
        val historyButton: CardView = binding.history
        val geographyButton: CardView = binding.geography
        val buttons = listOf(
            historyButton,
            portugueseButton,
            mathButton,
            scienceButton,
            geographyButton
        )
        val continueButton: ConstraintLayout = binding.continueButton
        val subjectViewModel = ViewModelProvider(this)[SubjectViewModel::class.java]

        homeViewModel.config.observe(viewLifecycleOwner) { json ->
            val nomeUsuario = json.optString("nome_usuario")
            nameView.text = nomeUsuario
            val formacao = json.optString("formacao")
            formacaoView.text = "$formacao"
            val idade = json.optString("idade")
            idadeView.text = "$idade anos"
            val disciplina = json.optString("ultima_disciplina")

            val disciplina_lower = disciplina.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            disciplinaView.text = disciplina_lower

            val rankingJson = json.optJSONObject("ranking") ?: return@observe
            val keysRanking = rankingJson.keys()

            while (keysRanking.hasNext()) {
                val key = keysRanking.next()
                val obj = rankingJson.optJSONObject(key) ?: continue
                val nome = obj.optString("nome")

                val iconString = obj.optString("icon")

                val iconName = iconString
                    .removePrefix("@images/")
                    .removePrefix("@drawable/")

                val iconRes = if (iconName.isNotBlank()) {
                    resources.getIdentifier(iconName, "drawable", requireContext().packageName)
                } else {
                    0
                }
                if (nome == nomeUsuario) {
                    icon.setImageResource(iconRes)
                }
            }

            val disciplinas = json.optJSONArray("conteudo_disciplinas")

            if (disciplinas != null) {
                val max = minOf(disciplinas.length(), buttons.size)

                for (i in 0 until max) {
                    val disc = disciplinas.getJSONObject(i)
                    val button = buttons[i]
                    val disciplinaName = disc.optString("disciplina")

                    if (disciplinaName == disciplina) {
                        continueButton.setOnClickListener {
                            findNavController().navigate(
                                R.id.action_navigation_home_to_subject
                            )
                        }
                    }

                    button.setOnClickListener {
                        findNavController().navigate(
                            R.id.action_navigation_home_to_subject
                        )

                        json.put("ultima_disciplina",disciplinaName)
                        (requireActivity() as MainActivity).atualizarJson(json)

                        subjectViewModel.setConfig(disc)
                    }
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}