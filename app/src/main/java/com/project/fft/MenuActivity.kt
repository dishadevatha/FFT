package com.project.fft

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

data class MenuItem(
    val itemName: String = "",
    val image: String = "",
    val description: String = "",
    val price: Double = 0.0,
    var vendor: String=""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(itemName)
        parcel.writeString(image)
        parcel.writeString(description)
        parcel.writeDouble(price)
        parcel.writeString(vendor)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MenuItem> {
        override fun createFromParcel(parcel: Parcel): MenuItem {
            return MenuItem(parcel)
        }

        override fun newArray(size: Int): Array<MenuItem?> {
            return arrayOfNulls(size)
        }
    }
}

class MenuActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var menuRecyclerView: RecyclerView
    private lateinit var vendorMenuAdapter: MenuAdapter
    private lateinit var viewCartButton: ImageButton
    private val menuItems = mutableListOf<MenuItem>()
    private val cartItems = mutableListOf<MenuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)


        val menuCollection = intent.getStringExtra("menuCollection")

        menuRecyclerView = findViewById(R.id.menuRecyclerView)
        viewCartButton = findViewById(R.id.viewCartButton)
        menuRecyclerView.layoutManager = LinearLayoutManager(this)

        vendorMenuAdapter = MenuAdapter(menuItems) { menuItem ->
            // Handle the + button click to add to the cart
            menuItem.vendor = intent.getStringExtra("vendorName").toString()
            Toast.makeText(this, "${menuItem.itemName} added to cart", Toast.LENGTH_SHORT).show()
            cartItems.add(menuItem)
        }

        menuRecyclerView.adapter = vendorMenuAdapter


        if (menuCollection != null) {
            fetchMenuItems(menuCollection)
        }

        viewCartButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putParcelableArrayListExtra("cartItems", ArrayList(cartItems)) // Send the cart items to CartActivity
            startActivity(intent)
            cartItems.clear()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchMenuItems(menuCollection: String) {
        db.collection(menuCollection)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val item = document.toObject(MenuItem::class.java)
                    menuItems.add(item)
                }

                vendorMenuAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("MenuActivity", "Error fetching menu items", exception)
                Toast.makeText(this, "Error loading menu", Toast.LENGTH_SHORT).show()
            }
    }
}