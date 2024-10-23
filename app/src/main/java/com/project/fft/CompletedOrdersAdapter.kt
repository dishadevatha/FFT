package com.project.fft

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CompletedOrdersAdapter(
    private val completedOrders: List<CompletedOrder>,
    private val deleteOrder: (String) -> Unit // Lambda for handling delete action
) : RecyclerView.Adapter<CompletedOrdersAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderDetailsTextView: TextView = itemView.findViewById(R.id.orderDetailsTextView)
        val orderAmountTextView: TextView = itemView.findViewById(R.id.orderAmountTextView)
        val amountTextView: TextView = itemView.findViewById(R.id.AmountTextView)
        val deleteButton: Button = itemView.findViewById(R.id.completeOrderButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.completed_order_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = completedOrders[position]

        holder.orderDetailsTextView.text = order.items
        holder.orderAmountTextView.text = "Token Number: ${order.tokenNo}"
        holder.amountTextView.text = "Amount: ₹${order.amount}"

        holder.deleteButton.setOnClickListener {
            deleteOrder(order.id) // Call the lambda to delete the order
        }
    }

    override fun getItemCount(): Int = completedOrders.size
}
