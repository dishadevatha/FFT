package com.project.fft

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class PaymentActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var totalPrice: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        Checkout.preload(applicationContext)

        // Get the total price from the intent
        totalPrice = intent.getStringExtra("totalPrice").toString()
        startPayment(totalPrice)
    }

    private fun startPayment(amount: String) {
        try {
            val checkout = Checkout()
            // Set your Razorpay API key
            checkout.setKeyID("rzp_test_HyvhCdIVyGYqj9")
            val options = JSONObject()
            options.put("name", "Food For Thought")
            options.put("description", "Payment for cart items")
            options.put("currency", "INR")
            options.put("amount",amount)
            options.put("prefill.email", "test@razorpay.com") // Prefill user email
            options.put("prefill.contact", "1234567890") // Prefill user contact
            checkout.open(this, options)
        } catch (e: Exception) {
            Log.e("payment1", "Error in starting Razorpay Checkout")
            Toast.makeText(this, "Error in starting Razorpay Checkout", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    // This function handles the payment result (success or failure)
    override fun onPaymentSuccess(razorpayPaymentID: String) {
        Toast.makeText(this, "Payment Successful! Payment ID: $razorpayPaymentID", Toast.LENGTH_LONG).show()

        // Redirect to a success page or main menu after payment
        val intent = Intent(this, PaymentSuccessActivity::class.java)

        startActivity(intent)
        finish()
    }

    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(this, "Payment Failed: $response", Toast.LENGTH_LONG).show()
    }
}
