package com.project.fft

data class CompletedOrder(
    var id: String = "",          // Firestore document ID
    var items: String = "",     // Order details
    var tokenNo: Int = -1, // Token number
    var amount: Int = -1,      // Order amount
    var vendor: String = ""
)
