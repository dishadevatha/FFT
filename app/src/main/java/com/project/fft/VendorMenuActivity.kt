package com.project.fft

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class VendorMenuActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var menuAdapter: VendorMenuAdapter
    private lateinit var menuList: MutableList<VendorMenuItem>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var vendorName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_menu) // Refers to `edit_menu.xml`

        val sp = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        vendorName = sp.getString("vendorName", null).toString()

        recyclerView = findViewById(R.id.menu_items_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        menuList = mutableListOf()

        firestore = FirebaseFirestore.getInstance()

        // Fetch menu items from Firestore
        fetchMenuItems()

        // Add item button to redirect to AddMenuItemActivity
        findViewById<Button>(R.id.add_item_button).setOnClickListener {
            val intent = Intent(this, AddMenuItemActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchMenuItems() {
        firestore.collection("${vendorName} Menu")
            .get()
            .addOnSuccessListener { documents ->
                menuList.clear()
                for (document in documents) {
                    val item = document.toObject(VendorMenuItem::class.java)
                    menuList.add(item)
                }
                menuAdapter = VendorMenuAdapter(menuList) { menuItem -> deleteMenuItem(menuItem) }
                recyclerView.adapter = menuAdapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to load menu: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteMenuItem(menuItem: VendorMenuItem) {
        firestore.collection("${vendorName} Menu")
            .whereEqualTo("itemName", menuItem.itemName)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    firestore.collection("${vendorName} Menu").document(document.id).delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "${menuItem.itemName} deleted", Toast.LENGTH_SHORT).show()
                            fetchMenuItems() // Refresh the list
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to delete: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
    }
}