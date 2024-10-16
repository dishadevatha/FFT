package com.project.fft

data class ActiveOrder(
    var id: String = "",
    val items: String = "",
    val amount: Double = -1.0,
    val vendor: String = "",
    val tokenNo: Int = -1
)
