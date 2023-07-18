package com.ashishbhoi.expensetrackerkotlin.services

import com.ashishbhoi.expensetrackerkotlin.exceptions.EtAuthException
import com.ashishbhoi.expensetrackerkotlin.models.User
import com.ashishbhoi.expensetrackerkotlin.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.regex.Pattern

@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var userRepository: UserRepository


    override fun validateUser(email: String, password: String): User {
        val newEmail = email.lowercase(Locale.getDefault())
        return userRepository.findByEmailAndPassword(newEmail, password)
            ?: throw EtAuthException("Invalid email/password")
    }

    override fun registerUser(user: User): User {
        val pattern = Pattern.compile("^(.+)@(.+)$")
        val newEmail = user.email.lowercase(Locale.getDefault())
        if (!pattern.matcher(newEmail).matches()) throw EtAuthException("Invalid email format")
        val count = userRepository.getCountByEmail(newEmail)
        if (count > 0) throw EtAuthException("Email already in use")
        val userId = userRepository.create(user.firstName, user.lastName, user.email, user.password)
        return userRepository.findById(userId) ?: throw EtAuthException("Invalid user details")
    }
}