package com.ashishbhoi.expensetrackerkotlin.services

import com.ashishbhoi.expensetrackerkotlin.exceptions.EtBadRequestException
import com.ashishbhoi.expensetrackerkotlin.exceptions.EtNotFoundException
import com.ashishbhoi.expensetrackerkotlin.models.Transaction

interface TransactionService {
    fun fetchAllTransactions(userId: Int, categoryId: Int): List<Transaction>

    @Throws(EtNotFoundException::class)
    fun fetchTransactionById(userId: Int, categoryId: Int, transactionId: Int): Transaction

    @Throws(EtBadRequestException::class)
    fun addTransaction(userId: Int, categoryId: Int, transaction: Transaction): Transaction

    @Throws(EtBadRequestException::class)
    fun updateTransaction(userId: Int, categoryId: Int, transactionId: Int, transaction: Transaction)

    @Throws(EtNotFoundException::class)
    fun removeTransaction(userId: Int, categoryId: Int, transactionId: Int)
}