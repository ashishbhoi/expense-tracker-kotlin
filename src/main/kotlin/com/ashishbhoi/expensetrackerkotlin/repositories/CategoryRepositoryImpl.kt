package com.ashishbhoi.expensetrackerkotlin.repositories

import com.ashishbhoi.expensetrackerkotlin.exceptions.EtBadRequestException
import com.ashishbhoi.expensetrackerkotlin.models.Category
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement

@Repository
class CategoryRepositoryImpl : CategoryRepository {

    private val sqlFindAll = "SELECT C.CATEGORY_ID, C.USER_ID, C.TITLE, C.DESCRIPTION, COALESCE(SUM(T.AMOUNT), 0) " +
            "TOTAL_EXPENSE FROM ET_TRANSACTIONS T RIGHT OUTER JOIN ET_CATEGORIES C ON C.CATEGORY_ID = T.CATEGORY_ID " +
            "WHERE C.USER_ID = ? GROUP BY C.CATEGORY_ID"

    private val sqlFindById = "SELECT C.CATEGORY_ID, C.USER_ID, C.TITLE, C.DESCRIPTION, COALESCE(SUM(T.AMOUNT), 0) " +
            "TOTAL_EXPENSE FROM ET_TRANSACTIONS T RIGHT OUTER JOIN ET_CATEGORIES C ON C.CATEGORY_ID = T.CATEGORY_ID " +
            "WHERE C.USER_ID = ? AND C.CATEGORY_ID = ? GROUP BY C.CATEGORY_ID"

    private val sqlCreate = "INSERT INTO ET_CATEGORIES (CATEGORY_ID, USER_ID, TITLE, DESCRIPTION) " +
            "VALUES(NEXTVAL('ET_CATEGORIES_SEQ'), ?, ?, ?)"

    private val sqlUpdate = "UPDATE ET_CATEGORIES SET TITLE = ?, DESCRIPTION = ? WHERE USER_ID = ? AND CATEGORY_ID = ?"

    private val sqlDeleteCategory = "DELETE FROM ET_CATEGORIES WHERE USER_ID = ? AND CATEGORY_ID = ?"

    private val sqlDeleteAllTransactions = "DELETE FROM ET_TRANSACTIONS WHERE CATEGORY_ID = ?"

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    private val categoryRowMapper = RowMapper<Category> { rs, _ ->
        Category(
            rs.getInt("CATEGORY_ID"),
            rs.getInt("USER_ID"),
            rs.getString("TITLE"),
            rs.getString("DESCRIPTION"),
            rs.getDouble("TOTAL_EXPENSE")
        )
    }

    override fun findAll(userId: Int): List<Category> {
        try {
            val categories = jdbcTemplate.query({ connection ->
                val ps = connection.prepareStatement(sqlFindAll)
                ps.setInt(1, userId)
                ps
            }, categoryRowMapper)
            return categories
        } catch (e: Exception) {
            throw EtBadRequestException("Invalid request")
        }
    }

    override fun findById(userId: Int, categoryId: Int): Category? {
        try {
            val categories = jdbcTemplate.query({ connection ->
                val ps = connection.prepareStatement(sqlFindById)
                ps.setInt(1, userId)
                ps.setInt(2, categoryId)
                ps
            }, categoryRowMapper)
            return categories[0]
        } catch (e: java.lang.Exception) {
            throw EtBadRequestException("Invalid request")
        }
    }

    override fun create(userId: Int, title: String, description: String): Int {
        try {
            val keyHolder = GeneratedKeyHolder()
            jdbcTemplate.update({ connection ->
                val ps = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS)
                ps.setInt(1, userId)
                ps.setString(2, title)
                ps.setString(3, description)
                ps
            }, keyHolder)
            return keyHolder.keys!!["CATEGORY_ID"] as Int
        } catch (e: Exception) {
            throw EtBadRequestException("Invalid request")
        }
    }

    override fun update(userId: Int, categoryId: Int, category: Category) {
        try {
            jdbcTemplate.update { connection ->
                val ps = connection.prepareStatement(sqlUpdate)
                ps.setString(1, category.title)
                ps.setString(2, category.description)
                ps.setInt(3, userId)
                ps.setInt(4, categoryId)
                ps
            }
        } catch (e: Exception) {
            throw EtBadRequestException("Invalid request")
        }
    }

    override fun removeById(userId: Int, categoryId: Int) {
        try {
            deleteAllTransactions(categoryId)
            jdbcTemplate.update { connection ->
                val ps = connection.prepareStatement(sqlDeleteCategory)
                ps.setInt(1, userId)
                ps.setInt(2, categoryId)
                ps
            }
        } catch (e: Exception) {
            throw EtBadRequestException("Invalid request")
        }
    }

    fun deleteAllTransactions(categoryId: Int) {
        try {
            jdbcTemplate.update { connection ->
                val ps = connection.prepareStatement(sqlDeleteAllTransactions)
                ps.setInt(1, categoryId)
                ps
            }
        } catch (e: Exception) {
            throw EtBadRequestException("Invalid request")
        }
    }
}