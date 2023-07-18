package com.ashishbhoi.expensetrackerkotlin.controllers

import com.ashishbhoi.expensetrackerkotlin.models.Transaction
import com.ashishbhoi.expensetrackerkotlin.services.TransactionService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/categories/{categoryId}/transactions")
class TransactionController {

    @Autowired
    lateinit var transactionService: TransactionService

    @RequestMapping("")
    fun getAllTransactions(request: HttpServletRequest, @PathVariable categoryId: Int)
            : ResponseEntity<List<Transaction>> {
        val userId = request.getAttribute("userId") as Int
        val transactions = transactionService.fetchAllTransactions(userId, categoryId)
        return ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK)
    }

    @RequestMapping("/{transactionId}")
    fun getTransactionById(
        request: HttpServletRequest, @PathVariable categoryId: Int,
        @PathVariable transactionId: Int
    ): ResponseEntity<Transaction> {
        val userId = request.getAttribute("userId") as Int
        val transaction = transactionService.fetchTransactionById(userId, categoryId, transactionId)
        return ResponseEntity<Transaction>(transaction, HttpStatus.OK)
    }

    @PostMapping("")
    fun addTransaction(
        request: HttpServletRequest, @PathVariable categoryId: Int,
        @RequestBody transactionMap: Transaction
    ): ResponseEntity<Transaction> {
        val userId = request.getAttribute("userId") as Int
        val transaction = transactionService.addTransaction(userId, categoryId, transactionMap)
        return ResponseEntity<Transaction>(transaction, HttpStatus.CREATED)
    }

    @PutMapping("/{transactionId}")
    fun updateTransaction(
        request: HttpServletRequest, @PathVariable categoryId: Int,
        @PathVariable transactionId: Int, @RequestBody transactionMap: Transaction
    ): ResponseEntity<Map<String, Boolean>> {
        val userId = request.getAttribute("userId") as Int
        transactionService.updateTransaction(userId, categoryId, transactionId, transactionMap)
        return ResponseEntity<Map<String, Boolean>>(mapOf("success" to true), HttpStatus.OK)
    }

    @DeleteMapping("/{transactionId}")
    fun deleteTransaction(
        request: HttpServletRequest, @PathVariable categoryId: Int,
        @PathVariable transactionId: Int
    ): ResponseEntity<Map<String, Boolean>> {
        val userId = request.getAttribute("userId") as Int
        transactionService.removeTransaction(userId, categoryId, transactionId)
        return ResponseEntity<Map<String, Boolean>>(mapOf("success" to true), HttpStatus.OK)
    }
}