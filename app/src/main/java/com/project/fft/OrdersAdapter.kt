package com.project.fft

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrdersAdapter(
    private val orders: List<ActiveOrder>,
    private val onCompleteOrder: (String) -> Unit
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)
    }

    override fun getItemCount() = orders.size

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val orderDetailsTextView: TextView = itemView.findViewById(R.id.orderDetailsTextView)
        private val orderAmountTextView: TextView = itemView.findViewById(R.id.orderAmountTextView)
        private val completeOrderButton: Button = itemView.findViewById(R.id.completeOrderButton)

        @SuppressLint("SetTextI18n")
        fun bind(order: ActiveOrder) {
            orderDetailsTextView.text = "${order.items}"
            orderAmountTextView.text = "Token Number: ${order.tokenNo}"
            completeOrderButton.setOnClickListener {
                onCompleteOrder(order.id)  // Call the function to complete the order
            }
        }
    }
}
