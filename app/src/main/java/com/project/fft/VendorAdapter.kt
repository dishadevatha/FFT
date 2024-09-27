package com.project.fft

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class VendorAdapter(
    private val vendors: List<Vendor>,
    private val onClick: (Vendor) -> Unit // Define a click listener for each item
) : RecyclerView.Adapter<VendorAdapter.VendorViewHolder>() {

    class VendorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vendorName: TextView = itemView.findViewById(R.id.vendorName)
        val vendorImage: ImageView = itemView.findViewById(R.id.vendorImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vendor, parent, false)
        return VendorViewHolder(view)
    }

    override fun onBindViewHolder(holder: VendorViewHolder, position: Int) {
        val vendor = vendors[position]
        holder.vendorName.text = vendor.name

        // Load vendor image using Glide or any other image loading library
        Glide.with(holder.itemView.context)
            .load(vendor.image)
            .into(holder.vendorImage)

        // Set click listener for each item
        holder.itemView.setOnClickListener {
            onClick(vendor)
        }
    }

    override fun getItemCount() = vendors.size
}
