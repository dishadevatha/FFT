package com.project.fft

data class VendorMenuItem(
    val itemName: String = "",
    val description: String = "",
    val price: Int = 0,
    val image: String = "" // URL of image stored in Firebase Storage
)
