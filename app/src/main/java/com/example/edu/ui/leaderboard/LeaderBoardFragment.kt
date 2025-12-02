package com.example.edu.ui.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edu.databinding.FragmentLeaderboardBinding
import com.example.edu.ui.RankingAdapter.RankingAdapter
import com.example.edu.ui.config.LeaderBoardViewModel
import com.example.edu.ui.home.RankingItem
import kotlin.getValue

class LeaderBoardFragment : Fragment() {

    private val leaderboardViewModel: LeaderBoardViewModel by activityViewModels()
    private var _binding: FragmentLeaderboardBinding? = null
    private lateinit var rankingAdapter: RankingAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.comemorativeText
        val recycler: RecyclerView = binding.recyclerList
        val podium1: ImageView = binding.podium1Image
        val podium2: ImageView = binding.podium2Image
        val podium3: ImageView = binding.podium3Image
        rankingAdapter = RankingAdapter()
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = rankingAdapter

        leaderboardViewModel.config.observe(viewLifecycleOwner) { json ->
            val rankingJson = json.optJSONObject("ranking") ?: return@observe
            val nomeUsuario = json.optString("nome_usuario")

            val items = mutableListOf<RankingItem>()
            val keys = rankingJson.keys()

            while (keys.hasNext()) {
                val key = keys.next()
                val posicao = key.toIntOrNull() ?: continue
                val obj = rankingJson.optJSONObject(key) ?: continue

                val nome = obj.optString("nome")

                if (posicao <= 3 && nome == nomeUsuario) {
                    textView.text = "PARABÉNS ${nome.uppercase()}!"
                } else if (posicao > 3 && nome == nomeUsuario) {
                    textView.text = "CONTINUE TENTANDO ${nome.uppercase()}!"
                }

                val pontos = obj.optInt("pontos")
                val iconString = obj.optString("icon") // ex: "@images/blue"

                val iconName = iconString
                    .removePrefix("@images/")
                    .removePrefix("@drawable/")

                val iconRes = if (iconName.isNotBlank()) {
                    resources.getIdentifier(iconName, "drawable", requireContext().packageName)
                } else {
                    0
                }

                when (posicao) {
                    1 -> podium1.setImageResource(iconRes)
                    2 -> podium2.setImageResource(iconRes)
                    3 -> podium3.setImageResource(iconRes)
                }

                items.add(
                    RankingItem(
                        posicao = posicao,
                        nome = nome,
                        pontos = pontos,
                        iconRes = iconRes
                    )
                )
            }

            items.sortBy { it.posicao }

            rankingAdapter.submitList(items)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}