package com.example.edu.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.edu.databinding.FragmentHomeBinding

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

        homeViewModel.config.observe(viewLifecycleOwner) { json ->
            val nomeUsuario = json.optString("nome_usuario")
            nameView.text = nomeUsuario
            val formacao = json.optString("formacao")
            formacaoView.text = "$formacao"
            val idade = json.optString("idade")
            idadeView.text = "$idade anos"
            val disciplina = json.optString("ultima_disciplina")
                .lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            disciplinaView.text = disciplina

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
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}