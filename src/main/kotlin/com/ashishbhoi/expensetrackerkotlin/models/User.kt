package com.ashishbhoi.expensetrackerkotlin.models

data class User(
    val userId: Int,
    val firstName: String = "",
    val lastName: String = "",
    val email: String,
    val password: String
)
