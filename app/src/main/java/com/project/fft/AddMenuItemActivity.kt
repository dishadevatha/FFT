package com.project.fft

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


class AddMenuItemActivity : AppCompatActivity() {

    private lateinit var itemNameInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var priceInput: EditText
    private lateinit var selectImageButton: Button
    private lateinit var addItemButton: Button
    private lateinit var imagePreview: ImageView
    private lateinit var selectedImageUri: Uri
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storageReference: StorageReference
    private lateinit var vendorName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_menu_item)

        itemNameInput = findViewById(R.id.item_name_input)
        descriptionInput = findViewById(R.id.item_description_input)
        priceInput = findViewById(R.id.item_price_input)
        selectImageButton = findViewById(R.id.select_image_button)
        addItemButton = findViewById(R.id.add_item_button)
        imagePreview = findViewById(R.id.item_image_preview)

        firestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        val sp = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        vendorName = sp.getString("vendorName", null).toString()

        selectImageButton.setOnClickListener {
            selectImage()
        }

        addItemButton.setOnClickListener {
            uploadMenuItem()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data!!
            imagePreview.setImageURI(selectedImageUri)
        }
    }

    private fun uploadMenuItem() {
        val itemName = itemNameInput.text.toString()
        val description = descriptionInput.text.toString()
        val price = priceInput.text.toString().toInt()

        Log.e("AddMenuItemActivity_", "Item Name: $itemName")
        Log.e("AddMenuItemActivity_", "Description: $description")
        Log.e("AddMenuItemActivity_", "Price: $price")
        Log.e("AddMenuItemActivity_", "Selected Image URI: $selectedImageUri")
        Log.e("AddMenuItemActivity_", "Vendor Name: $vendorName")


        val imageRef = storageReference.child("${selectedImageUri.lastPathSegment}")
        Log.e("AddMenuItemActivity_", "Image Ref: $imageRef")
        imageRef.putFile(selectedImageUri).addOnSuccessListener { taskSnapshot  ->
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                val menuItem = VendorMenuItem(itemName, description, price, uri.toString())
                Log.e("AddMenuItemActivity_", "Menu Item $menuItem")
                firestore.collection("${vendorName} Menu")
                    .add(menuItem)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to add item: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("AddMenuItemActivity_", "Failed to upload image: ${e.message}")
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}