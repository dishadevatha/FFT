package com.project.fft

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.fft.R
import com.project.fft.VendorMenuItem
import com.squareup.picasso.Picasso

class VendorMenuAdapter(
    private val menuList: List<VendorMenuItem>,
    private val onDeleteClicked: (VendorMenuItem) -> Unit
) : RecyclerView.Adapter<VendorMenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.menu_item_name)
        val description: TextView = itemView.findViewById(R.id.menu_item_description)
        val price: TextView = itemView.findViewById(R.id.menu_item_price)
        val itemImage: ImageView = itemView.findViewById(R.id.menu_item_image)
        val deleteButton: ImageView = itemView.findViewById(R.id.menu_remove_item_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.edit_menu_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = menuList[position]

        holder.itemName.text = menuItem.itemName
        holder.description.text = menuItem.description
        holder.price.text = "₹${menuItem.price}"
        Picasso.get().load(menuItem.image).into(holder.itemImage) // Using Picasso for image loading

        // Handle delete button click
        holder.deleteButton.setOnClickListener {
            onDeleteClicked(menuItem)
        }
    }

    override fun getItemCount() = menuList.size
}

