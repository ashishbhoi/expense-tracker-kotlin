package com.ashishbhoi.expensetrackerkotlin

import com.ashishbhoi.expensetrackerkotlin.filters.AuthFilter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@SpringBootApplication
class ExpenseTrackerKotlinApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<ExpenseTrackerKotlinApplication>(*args)
        }
    }

    @Bean
    fun corsFilter(): FilterRegistrationBean<CorsFilter> {
        val registrationBean = FilterRegistrationBean<CorsFilter>()
        val source = UrlBasedCorsConfigurationSource()
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.addAllowedOrigin("*")
        corsConfiguration.addAllowedHeader("*")
        source.registerCorsConfiguration("/**", corsConfiguration)
        registrationBean.filter = CorsFilter(source)
        registrationBean.order = 0
        return registrationBean
    }

    @Bean
    fun filterFilterRegistrationBean(): FilterRegistrationBean<AuthFilter> {
        val registrationBean: FilterRegistrationBean<AuthFilter> = FilterRegistrationBean<AuthFilter>()
        val authFilter = AuthFilter()
        registrationBean.filter = authFilter
        registrationBean.addUrlPatterns("/api/categories/*")
        return registrationBean
    }
}