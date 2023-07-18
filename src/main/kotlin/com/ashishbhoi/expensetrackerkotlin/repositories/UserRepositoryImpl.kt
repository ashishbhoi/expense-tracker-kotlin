package com.ashishbhoi.expensetrackerkotlin.repositories

import com.ashishbhoi.expensetrackerkotlin.models.User
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

@Repository
class UserRepositoryImpl : UserRepository {

    private val create = "INSERT INTO ET_USERS(USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD) " +
            "VALUES(NEXTVAL('ET_USERS_SEQ'), ?, ?, ?, ?)"

    private val findByEmailAndPassword: String = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD " +
            "FROM ET_USERS WHERE EMAIL = ?"

    private val countByEmail = "SELECT COUNT(*) FROM ET_USERS WHERE EMAIL = ?"

    private val findById = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD FROM ET_USERS WHERE USER_ID = ?"

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    private val userRowMapper = RowMapper<User> { rs, _ ->
        User(
            rs.getInt("USER_ID"),
            rs.getString("FIRST_NAME"),
            rs.getString("LAST_NAME"),
            rs.getString("EMAIL"),
            rs.getString("PASSWORD")
        )
    }

    override fun create(firstName: String, lastName: String, email: String, password: String): Int {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10))
        try {
            val keyHolder: KeyHolder = GeneratedKeyHolder()
            jdbcTemplate.update({ connection: Connection ->
                val ps = connection.prepareStatement(create, Statement.RETURN_GENERATED_KEYS)
                ps.setString(1, firstName)
                ps.setString(2, lastName)
                ps.setString(3, email)
                ps.setString(4, hashedPassword)
                ps
            }, keyHolder)
            return keyHolder.keys!!["USER_ID"] as Int
        } catch (e: Exception) {
            throw e
        }
    }

    override fun findByEmailAndPassword(email: String, password: String): User? {
        val users = jdbcTemplate.query({ connection ->
            val ps = connection.prepareStatement(findByEmailAndPassword)
            ps.setString(1, email)
            ps
        }, userRowMapper)
        if (users.size == 0) {
            throw Exception("Invalid email/password")
        }
        val user = users[0]
        if (!BCrypt.checkpw(password, user.password)) {
            throw Exception("Invalid email/password")
        }
        return user
    }

    override fun getCountByEmail(email: String): Int {
        val count = jdbcTemplate.query<Int>({ connection: Connection ->
            val ps = connection.prepareStatement(countByEmail)
            ps.setString(1, email)
            ps
        }) { rs: ResultSet, _: Int -> rs.getInt(1) }
        return count[0]
    }

    override fun findById(userId: Int): User? {
        val users = jdbcTemplate.query({ connection ->
            val ps = connection.prepareStatement(findById)
            ps.setInt(1, userId)
            ps
        }, userRowMapper)
        return users[0]
    }
}