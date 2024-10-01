package com.project.fft

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MenuAdapter(
    private val menuItems: List<MenuItem>,
    private val onAddToCartClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameTextView: TextView = itemView.findViewById(R.id.itemName)
        val itemDescriptionTextView: TextView = itemView.findViewById(R.id.itemDescription)
        val itemPriceTextView: TextView = itemView.findViewById(R.id.itemPrice)
        val itemImageView: ImageView = itemView.findViewById(R.id.itemImage)
        val addToCartButton: ImageView = itemView.findViewById(R.id.addToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.menu_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = menuItems[position]

        holder.itemNameTextView.text = menuItem.itemName
        holder.itemDescriptionTextView.text = menuItem.description
        holder.itemPriceTextView.text = "₹${menuItem.price}"

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(menuItem.image)
            .into(holder.itemImageView)

        // Handle add to cart button click
        holder.addToCartButton.setOnClickListener {
            onAddToCartClick(menuItem)
        }
    }

    override fun getItemCount(): Int = menuItems.size
}