package com.ashishbhoi.expensetrackerkotlin.controllers

import com.ashishbhoi.expensetrackerkotlin.Constants
import com.ashishbhoi.expensetrackerkotlin.models.User
import com.ashishbhoi.expensetrackerkotlin.services.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/users")
class UserController {
    @Autowired
    lateinit var userService: UserService

    private fun generateJWTToken(user: User): Map<String, String> {
        val timestamp = System.currentTimeMillis()
        val token: String = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
            .setIssuedAt(Date(timestamp))
            .setExpiration(Date(timestamp + Constants.TOKEN_VALIDITY))
            .claim("userId", user.userId)
            .claim("firstName", user.firstName)
            .claim("lastName", user.lastName)
            .claim("email", user.email)
            .compact()
        val map: MutableMap<String, String> = HashMap()
        map["token"] = token
        return map
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody userMap: User): ResponseEntity<Map<String, String>> {
        val user = userService.validateUser(userMap.email, userMap.password)
        return ResponseEntity<Map<String, String>>(generateJWTToken(user), HttpStatus.OK)
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody userMap: User): ResponseEntity<Map<String, String>> {
        val user = userService.registerUser(userMap)
        return ResponseEntity<Map<String, String>>(generateJWTToken(user), HttpStatus.CREATED)
    }
}