package com.ashishbhoi.expensetrackerkotlin.services

import com.ashishbhoi.expensetrackerkotlin.models.User

interface UserService {
    fun validateUser(email: String, password: String): User
    fun registerUser(user: User): User
}