package com.ashishbhoi.expensetrackerkotlin.repositories

import com.ashishbhoi.expensetrackerkotlin.exceptions.EtAuthException
import com.ashishbhoi.expensetrackerkotlin.models.User

interface UserRepository {
    @Throws(EtAuthException::class)
    fun create(firstName: String, lastName: String, email: String, password: String): Int

    @Throws(EtAuthException::class)
    fun findByEmailAndPassword(email: String, password: String): User?

    @Throws(EtAuthException::class)
    fun getCountByEmail(email: String): Int

    @Throws(EtAuthException::class)
    fun findById(userId: Int): User?
}