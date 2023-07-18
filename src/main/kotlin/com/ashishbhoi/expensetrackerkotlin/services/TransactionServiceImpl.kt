package com.ashishbhoi.expensetrackerkotlin.services

import com.ashishbhoi.expensetrackerkotlin.exceptions.EtBadRequestException
import com.ashishbhoi.expensetrackerkotlin.exceptions.EtNotFoundException
import com.ashishbhoi.expensetrackerkotlin.models.Transaction
import com.ashishbhoi.expensetrackerkotlin.repositories.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TransactionServiceImpl : TransactionService {

    @Autowired
    lateinit var transactionRepository: TransactionRepository

    override fun fetchAllTransactions(userId: Int, categoryId: Int): List<Transaction> {
        return transactionRepository.findAll(userId, categoryId)
    }

    override fun fetchTransactionById(userId: Int, categoryId: Int, transactionId: Int): Transaction {
        return transactionRepository.findById(userId, categoryId, transactionId)
            ?: throw EtNotFoundException("Transaction not found")
    }

    override fun addTransaction(userId: Int, categoryId: Int, transaction: Transaction): Transaction {
        val transactionId = transactionRepository.create(
            userId, categoryId, transaction.amount, transaction.note,
            transaction.transactionDate
        )
        return transactionRepository.findById(userId, categoryId, transactionId)
            ?: throw EtBadRequestException("Invalid request")
    }

    override fun updateTransaction(userId: Int, categoryId: Int, transactionId: Int, transaction: Transaction) {
        val count = transactionRepository.update(userId, categoryId, transactionId, transaction)
        if (count.equals(0)) throw EtNotFoundException("Transaction not found")
    }

    override fun removeTransaction(userId: Int, categoryId: Int, transactionId: Int) {
        val count = transactionRepository.removeById(userId, categoryId, transactionId)
        if (count.equals(0)) throw EtNotFoundException("Transaction not found")
    }
}