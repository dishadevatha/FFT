package com.project.fft

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class vendor_login : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var logoImageView: ImageView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vendor_login)

        nameEditText = findViewById(R.id.editTextText2)
        passwordEditText = findViewById(R.id.editTextTextPassword)
        loginButton = findViewById(R.id.login_button)
        logoImageView = findViewById(R.id.imageView3)

        // Set an onClick listener for the login button
        loginButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (validateInput(name, password)) {
                authenticateVendor(name, password)
            }
        }
    }

    // Function to validate user input
    private fun validateInput(name: String, password: String): Boolean {
        return when {
            name.isEmpty() -> {
                nameEditText.error = "Name cannot be empty"
                nameEditText.requestFocus()
                false
            }
            password.isEmpty() -> {
                passwordEditText.error = "Password cannot be empty"
                passwordEditText.requestFocus()
                false
            }
            else -> true
        }
    }

    // Function to authenticate vendor by checking Firestore
    private fun authenticateVendor(name: String, password: String) {
        db.collection("Vendor")
            .whereEqualTo("name", name)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Vendor found
                    val vendor = documents.documents[0]

                    // Extract vendor data (like UPI_id, image) if needed
                    val vendorName = vendor.getString("name")
                    val vendorUPI = vendor.getString("UPI_id")
                    val vendorImage = vendor.getString("image")

                    // Successful login (Navigate to the vendor's home/dashboard page)
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, activeOrders::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "Incorrect name or password", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("VendorLogin", "Error querying Firestore", exception)
                Toast.makeText(this, "Login failed. Please try again later.", Toast.LENGTH_SHORT).show()
            }
    }
}
