package com.seedling.demo.hostdeveloperdemo

import android.os.Looper
import android.renderscript.ScriptIntrinsicYuvToRGB
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

class CardSelectionAdapter(
    private var cardList : List<Card>,
    private val onItemSelectedListener: (position: Int, card: Card) -> Unit
) : RecyclerView.Adapter<CardSelectionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivBlackCard: ImageView = itemView.findViewById(R.id.ivBlackCard)
        val tvCardNumber: TextView = itemView.findViewById(R.id.tvCardNumber)
        val radioButton: RadioButton = itemView.findViewById(R.id.radioButton)
    }

    companion object {
        lateinit var card: Card
    }


    private val handler = android.os.Handler(Looper.getMainLooper())
    private lateinit var view: View


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        card = cardList[position]

        holder.tvCardNumber.text = card.cardNumber

        holder.radioButton.isChecked = position == MainActivity.cardSelectedPosition

        // Set a click listener on the entire item view
        holder.itemView.setOnClickListener {
            // Update the selectedPosition and notify data set changed
            MainActivity.cardSelectedPosition = holder.adapterPosition
            onItemSelectedListener.invoke(position, card)
            notifyDataSetChanged()
        }

        holder.radioButton.setOnClickListener {
            // Update the selectedPosition and notify data set changed
            MainActivity.cardSelectedPosition = holder.adapterPosition
            onItemSelectedListener.invoke(position, card)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }

}