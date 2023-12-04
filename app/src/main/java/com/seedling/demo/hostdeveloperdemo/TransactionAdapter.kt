package com.seedling.demo.hostdeveloperdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(private val transactionList: List<TransactionItem>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val historyTextView: TextView = itemView.findViewById(R.id.tvHistory)
        val amountTextView: TextView = itemView.findViewById(R.id.tvAmount_transaction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_history, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.historyTextView.text = transaction.history
        holder.amountTextView.text = transaction.amount
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }
}
