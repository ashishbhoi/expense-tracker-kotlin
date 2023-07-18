package com.ashishbhoi.expensetrackerkotlin.repositories

import com.ashishbhoi.expensetrackerkotlin.exceptions.EtBadRequestException
import com.ashishbhoi.expensetrackerkotlin.models.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement

@Repository
class TransactionRepositoryImpl : TransactionRepository {

    private val sqlFindAll = "SELECT TRANSACTION_ID, CATEGORY_ID, USER_ID, AMOUNT, NOTE, TRANSACTION_DATE " +
            "FROM ET_TRANSACTIONS WHERE USER_ID = ? AND CATEGORY_ID = ?"

    private val sqlFindId = "SELECT TRANSACTION_ID, CATEGORY_ID, USER_ID, AMOUNT, NOTE, TRANSACTION_DATE " +
            "FROM ET_TRANSACTIONS WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSACTION_ID = ?"

    private val sqlCreate = "INSERT INTO ET_TRANSACTIONS(TRANSACTION_ID, CATEGORY_ID, USER_ID, AMOUNT, NOTE," +
            " TRANSACTION_DATE) VALUES(NEXTVAL('ET_TRANSACTIONS_SEQ'), ?, ?, ?, ?, ?)"

    private val sqlUpdate = "UPDATE ET_TRANSACTIONS SET AMOUNT = ?, NOTE = ?, TRANSACTION_DATE = ? " +
            "WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSACTION_ID = ?"

    private val sqlRemove = "DELETE FROM ET_TRANSACTIONS WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSACTION_ID = ?"

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    private val transactionRowMapper = RowMapper<Transaction> { rs, _ ->
        Transaction(
            rs.getInt("TRANSACTION_ID"),
            rs.getInt("CATEGORY_ID"),
            rs.getInt("USER_ID"),
            rs.getDouble("AMOUNT"),
            rs.getString("NOTE"),
            rs.getLong("TRANSACTION_DATE")
        )
    }

    override fun findAll(userId: Int, categoryId: Int): List<Transaction> {
        try {
            val transactions = jdbcTemplate.query({ connection ->
                val ps = connection.prepareStatement(sqlFindAll)
                ps.setInt(1, userId)
                ps.setInt(2, categoryId)
                ps
            }, transactionRowMapper)
            return transactions
        } catch (e: Exception) {
            throw EtBadRequestException("Invalid request")
        }
    }

    override fun findById(userId: Int, categoryId: Int, transactionId: Int): Transaction? {
        try {
            val transactions = jdbcTemplate.query({ connection ->
                val ps = connection.prepareStatement(sqlFindId)
                ps.setInt(1, userId)
                ps.setInt(2, categoryId)
                ps.setInt(3, transactionId)
                ps
            }, transactionRowMapper)
            return transactions[0]
        } catch (e: Exception) {
            throw EtBadRequestException("Invalid request")
        }
    }

    override fun create(userId: Int, categoryId: Int, amount: Double, note: String, transactionDate: Long): Int {
        try {
            val keyHolder = GeneratedKeyHolder()
            jdbcTemplate.update({ connection ->
                val ps = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS)
                ps.setInt(1, categoryId)
                ps.setInt(2, userId)
                ps.setDouble(3, amount)
                ps.setString(4, note)
                ps.setLong(5, transactionDate)
                ps
            }, keyHolder)
            return keyHolder.keys!!["TRANSACTION_ID"] as Int
        } catch (e: Exception) {
            throw EtBadRequestException("Invalid request")
        }
    }

    override fun update(userId: Int, categoryId: Int, transactionId: Int, transaction: Transaction) {
        try {
            jdbcTemplate.update { connection ->
                val ps = connection.prepareStatement(sqlUpdate)
                ps.setDouble(1, transaction.amount)
                ps.setString(2, transaction.note)
                ps.setLong(3, transaction.transactionDate)
                ps.setInt(4, userId)
                ps.setInt(5, categoryId)
                ps.setInt(6, transactionId)
                ps
            }
        } catch (e: Exception) {
            throw EtBadRequestException("Invalid request")
        }
    }

    override fun removeById(userId: Int, categoryId: Int, transactionId: Int) {
        try {
            jdbcTemplate.update { connection ->
                val ps = connection.prepareStatement(sqlRemove)
                ps.setInt(1, userId)
                ps.setInt(2, categoryId)
                ps.setInt(3, transactionId)
                ps
            }
        } catch (e: Exception) {
            throw EtBadRequestException("Invalid request")
        }
    }
}