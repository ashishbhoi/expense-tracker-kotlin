package com.ashishbhoi.expensetrackerkotlin.models

data class Category(
    val categoryId: Int,
    val userId: Int,
    val title: String = "",
    val description: String = "",
    val totalExpenses: Double
)
