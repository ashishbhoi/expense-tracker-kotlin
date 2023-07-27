package com.ashishbhoi.expensetrackerkotlin.filters

import com.ashishbhoi.expensetrackerkotlin.Constants
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import jakarta.servlet.FilterChain
import jakarta.servlet.GenericFilter
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus

class AuthFilter : GenericFilter() {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        val cookies = httpRequest.cookies
        val token = cookies.find { it.name == "Authorization" }?.value
        if (token != null) {
            try {
                val claims: Claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY)
                    .parseClaimsJws(token).body
                httpRequest.setAttribute("userId", claims["userId"].toString().toInt())
            } catch (e: Exception) {
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Invalid/expired login")
                return
            }
        } else {
            httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Please login to access this resource")
            return
        }
        chain?.doFilter(request, response)
    }
}