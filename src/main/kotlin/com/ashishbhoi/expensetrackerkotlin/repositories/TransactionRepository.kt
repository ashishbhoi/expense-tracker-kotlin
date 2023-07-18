package com.ashishbhoi.expensetrackerkotlin.repositories

import com.ashishbhoi.expensetrackerkotlin.exceptions.EtBadRequestException
import com.ashishbhoi.expensetrackerkotlin.exceptions.EtNotFoundException
import com.ashishbhoi.expensetrackerkotlin.models.Transaction

interface TransactionRepository {
    fun findAll(userId: Int, categoryId: Int): List<Transaction>

    @Throws(EtNotFoundException::class)
    fun findById(userId: Int, categoryId: Int, transactionId: Int): Transaction?

    @Throws(EtBadRequestException::class)
    fun create(userId: Int, categoryId: Int, amount: Double, note: String, transactionDate: Long): Int

    @Throws(EtBadRequestException::class)
    fun update(userId: Int, categoryId: Int, transactionId: Int, transaction: Transaction)

    @Throws(EtNotFoundException::class)
    fun removeById(userId: Int, categoryId: Int, transactionId: Int)
}