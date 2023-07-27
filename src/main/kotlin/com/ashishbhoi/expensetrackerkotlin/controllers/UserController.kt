package com.ashishbhoi.expensetrackerkotlin.controllers

import com.ashishbhoi.expensetrackerkotlin.Constants
import com.ashishbhoi.expensetrackerkotlin.models.User
import com.ashishbhoi.expensetrackerkotlin.services.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
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

    private fun generateCookie(token: String): Cookie {
        val cookie = Cookie("Authorization", token)
        cookie.path = "/"
        cookie.maxAge = 3600 * 24
        cookie.isHttpOnly = true
        cookie.secure = true
        return cookie
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody userMap: User, response: HttpServletResponse): ResponseEntity<Map<String, String>> {
        val user = userService.validateUser(userMap.email, userMap.password)
        val cookie = generateCookie(generateJWTToken(user)["token"].toString())
        response.addCookie(cookie)
        return ResponseEntity<Map<String, String>>(mapOf("message" to "User Logged in successfully"), HttpStatus.OK)
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody userMap: User, response: HttpServletResponse): ResponseEntity<Map<String, String>> {
        val user = userService.registerUser(userMap)
        val cookie = generateCookie(generateJWTToken(user)["token"].toString())
        response.addCookie(cookie)
        return ResponseEntity<Map<String, String>>(
            mapOf("message" to "User Created And Logged in successfully"),
            HttpStatus.CREATED
        )
    }

    @GetMapping("/logout")
    fun logoutUser(response: HttpServletResponse): ResponseEntity<Map<String, String>> {
        val cookie = Cookie("Authorization", "")
        cookie.maxAge = 0
        response.addCookie(cookie)
        return ResponseEntity<Map<String, String>>(mapOf("message" to "User Logged out successfully"), HttpStatus.OK)
    }
}