package com.example.edu.ui.RankingAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edu.R
import com.example.edu.ui.home.RankingItem

class RankingAdapter : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    private val itens = mutableListOf<RankingItem>()

    fun submitList(novaLista: List<RankingItem>) {
        itens.clear()
        itens.addAll(novaLista)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ranking, parent, false)
        return RankingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        holder.bind(itens[position])
    }

    override fun getItemCount(): Int = itens.size

    class RankingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textPosicao: TextView = itemView.findViewById(R.id.text_posicao)
        private val textNome: TextView = itemView.findViewById(R.id.text_nome)
        private val textPontos: TextView = itemView.findViewById(R.id.text_pontos)
        private val imageIcon: ImageView = itemView.findViewById(R.id.image_icon)

        fun bind(item: RankingItem) {
            textPosicao.text = item.posicao.toString()
            textNome.text = item.nome
            textPontos.text = "${item.pontos} pts"

            if (item.iconRes != 0) {
                imageIcon.setImageResource(item.iconRes)
            } else {
                imageIcon.setImageResource(R.drawable.blue) // fallback
            }
        }
    }
}
