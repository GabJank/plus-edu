package com.example.edu.ui.achievements

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.edu.databinding.FragmentAchievementBinding
import com.example.edu.ui.config.AchievementsViewModel
import com.example.edu.ui.home.RankingItem

class AchievementsFragment : Fragment() {
    private val achievementsViewModel: AchievementsViewModel by activityViewModels()
    private var _binding: FragmentAchievementBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAchievementBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val text_nome : TextView = binding.textNome
        val text_pontos : TextView = binding.textPontos
        val text_2_nome : TextView = binding.text2Nome
        val text_2_pontos : TextView = binding.text2Pontos
        val text_3_nome : TextView = binding.text3Nome
        val text_3_pontos : TextView = binding.text3Pontos

        val loading : LinearLayout = binding.loading
        val loading_second : LinearLayout = binding.loadingSecond
        val loading_third : LinearLayout = binding.loadingThird

        val comemorative: TextView = binding.comemorativeText
        val icon: ImageView = binding.iconImage
        val position: TextView = binding.textPosition

        achievementsViewModel.config.observe(viewLifecycleOwner) { json ->
            val achievements = json.optJSONObject("achievements") ?: return@observe
            val keys = achievements.keys()

            while (keys.hasNext()) {
                val key = keys.next()
                val posicao = key.toIntOrNull() ?: continue
                val obj = achievements.optJSONObject(key) ?: continue

                val nome = obj.optString("nome")
                val quantidade = obj.optString("quantidade")

                val partes = quantidade.split("/")
                val numerador = partes[0].toFloatOrNull() ?: 0f
                val denominador = partes[1].toFloatOrNull() ?: 1f

                val proporcao = numerador / denominador

                val maxWidth = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    360f,
                    resources.displayMetrics
                )

                val largura = (proporcao * maxWidth).toInt()

                when (posicao) {
                    1 -> {
                        text_nome.text = nome
                        text_pontos.text = quantidade
                        loading.layoutParams.width = largura
                        loading.requestLayout()
                    }
                    2 -> {
                        text_2_nome.text = nome
                        text_2_pontos.text = quantidade
                        loading_second.layoutParams.width = largura
                        loading_second.requestLayout()
                    }
                    3 -> {
                        text_3_nome.text = nome
                        text_3_pontos.text = quantidade
                        loading_third.layoutParams.width = largura
                        loading_third.requestLayout()
                    }
                }
            }

            val rankingJson = json.optJSONObject("ranking") ?: return@observe
            val nomeUsuario = json.optString("nome_usuario")
            val keysRanking = rankingJson.keys()

            while (keysRanking.hasNext()) {
                val key = keysRanking.next()
                val posicao = key.toIntOrNull() ?: continue
                val obj = rankingJson.optJSONObject(key) ?: continue
                val nome = obj.optString("nome")

                if (posicao <= 3 && nome == nomeUsuario) {
                    comemorative.text = "PARABÉNS!"
                } else if (posicao > 3 && nome == nomeUsuario) {
                    comemorative.text = "CONTINUE TENTANDO!"
                }

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
                    position.text = "$posicao"
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