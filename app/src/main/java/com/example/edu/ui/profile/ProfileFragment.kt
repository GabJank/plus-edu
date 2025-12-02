package com.example.edu.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.edu.databinding.FragmentProfileBinding
import kotlin.getValue

class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val data: TextView = binding.data
        val email: TextView = binding.email
        val numero: TextView = binding.numero
        val estado: TextView = binding.estado
        val nameView: TextView = binding.nameHome
        val idadeView: TextView = binding.idadeHome
        val formacaoView: TextView = binding.formacaoHome
        val icon: ImageView = binding.iconImage

        profileViewModel.config.observe(viewLifecycleOwner) { json ->
            val nomeUsuario = json.optString("nome_usuario")
            val formacao = json.optString("formacao")
            val idade = json.optString("idade")

            val nascimento = json.optString("nascimento")
            val mail = json.optString("email")
            val telefone = json.optString("telefone")
            val uf = json.optString("uf")

            nameView.text = nomeUsuario
            formacaoView.text = "$formacao"
            idadeView.text = "$idade anos"

            data.text = nascimento
            email.text = mail
            numero.text = telefone
            estado.text = uf

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