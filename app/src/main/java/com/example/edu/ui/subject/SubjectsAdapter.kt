package com.example.edu.ui.subject

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edu.R
import androidx.navigation.findNavController
import com.example.edu.MainActivity

class SubjectsAdapter(
    private val onItemClick: (SubjectItem) -> Unit
) : RecyclerView.Adapter<SubjectsAdapter.SubjectItemViewHolder>() {

    private val itens = mutableListOf<SubjectItem>()

    fun submitList(novaLista: List<SubjectItem>) {
        itens.clear()
        itens.addAll(novaLista)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subject, parent, false)
        return SubjectItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubjectItemViewHolder, position: Int) {
        val item = itens[position]
        holder.bind(item)

        val values_card =
            holder.itemView.findViewById<androidx.cardview.widget.CardView>(R.id.card)

        values_card.setOnClickListener {
            onItemClick(item)  // <-- aqui manda o item pra quem criou o adapter
        }
    }

    override fun getItemCount(): Int = itens.size

    class SubjectItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textPosicao: TextView = itemView.findViewById(R.id.subject_text)
        private val imageIcon: ImageView = itemView.findViewById(R.id.image_icon)

        fun bind(item: SubjectItem) {
            textPosicao.text = item.titulo

            if (item.avaliado) {
                imageIcon.setImageResource(R.drawable.check)
            } else {
                imageIcon.setImageResource(0)
            }
        }
    }
}