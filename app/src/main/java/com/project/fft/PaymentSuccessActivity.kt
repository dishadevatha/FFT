package com.project.fft

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random
import com.google.firebase.firestore.FirebaseFirestore

class PaymentSuccessActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        db = FirebaseFirestore.getInstance()

        val tokenNumberTextView = findViewById<TextView>(R.id.tokenNumber)
        val continueButton = findViewById<Button>(R.id.continueButton)

        val items = sharedPreferences.getString("items", "")
        val vendor = sharedPreferences.getString("vendor", "Unknown Vendor")
        val totalAmount = sharedPreferences.getString("amount", "0.0")

        // Generate a random token number
        val tokenNumber = generateRandomToken()
        tokenNumberTextView.text = tokenNumber.toString()

        // Save the order to DB
        saveOrderToFirestore(vendor ?: "", items ?: "", totalAmount?.toDouble() ?: 0.0, tokenNumber)


        continueButton.setOnClickListener {
            val intent = Intent(this, vendor_list::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun generateRandomToken(): Int {
        return Random.nextInt(1, 9999)
    }


    private fun saveOrderToFirestore(vendor: String, items: String, totalAmount: Double, tokenNumber: Int) {
        val orderData = hashMapOf(
            "vendor" to vendor,
            "items" to items,
            "amount" to totalAmount,
            "tokenNo" to tokenNumber
        )

        db.collection("Active Orders")
            .add(orderData)
            .addOnSuccessListener {
                Log.e("DataSaved","data Saved successfully")
            }
            .addOnFailureListener {
                Log.e("DataSaved","data not saved")
            }
    }
}
