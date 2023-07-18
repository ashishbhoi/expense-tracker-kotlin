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

        val authHeader = httpRequest.getHeader("Authorization")
        if (authHeader != null) {
            val authHeaderArr = authHeader.split("Bearer ".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            if (authHeaderArr.size > 1) {
                val token = authHeaderArr[1]
                try {
                    val claims: Claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY)
                        .parseClaimsJws(token).body
                    httpRequest.setAttribute("userId", claims["userId"].toString().toInt())
                } catch (e: Exception) {
                    httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Invalid/expired token")
                    return
                }
            } else {
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be Bearer [token]")
                return
            }
        } else {
            httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be provided")
            return
        }
        chain?.doFilter(request, response)
    }
}