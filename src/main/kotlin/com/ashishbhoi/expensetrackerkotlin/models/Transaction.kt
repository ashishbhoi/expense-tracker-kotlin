package com.ashishbhoi.expensetrackerkotlin.models

data class Transaction(
    val transactionId: Int,
    val categoryId: Int,
    val userId: Int,
    val amount: Double,
    val note: String = "",
    val transactionDate: Long
)
