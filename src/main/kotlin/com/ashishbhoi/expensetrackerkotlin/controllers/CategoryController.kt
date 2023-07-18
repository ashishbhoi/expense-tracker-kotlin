package com.ashishbhoi.expensetrackerkotlin.controllers

import com.ashishbhoi.expensetrackerkotlin.models.Category
import com.ashishbhoi.expensetrackerkotlin.services.CategoryService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/categories")
class CategoryController {

    @Autowired
    lateinit var categoryService: CategoryService

    @GetMapping("")
    fun getAllCategories(request: HttpServletRequest): ResponseEntity<List<Category>> {
        val userId = request.getAttribute("userId") as Int
        val categories: List<Category> = categoryService.fetchAllCategories(userId)
        return ResponseEntity<List<Category>>(categories, HttpStatus.OK)
    }

    @GetMapping("/{categoryId}")
    fun getCategoryById(request: HttpServletRequest, @PathVariable("categoryId") categoryId: Int)
            : ResponseEntity<Category> {
        val userId = request.getAttribute("userId") as Int
        val category: Category = categoryService.fetchCategoryById(userId, categoryId)
        return ResponseEntity<Category>(category, HttpStatus.OK)
    }

    @PostMapping("")
    fun addCategory(request: HttpServletRequest, @RequestBody categoryMap: Category): ResponseEntity<Category> {
        val userId = request.getAttribute("userId") as Int
        val category = categoryService.addCategory(userId, categoryMap)
        return ResponseEntity<Category>(category, HttpStatus.CREATED)
    }

    @PutMapping("/{categoryId}")
    fun updateCategory(
        request: HttpServletRequest, @PathVariable("categoryId") categoryId: Int,
        @RequestBody categoryMap: Category
    ): ResponseEntity<Map<String, Boolean>> {
        val userId = request.getAttribute("userId") as Int
        categoryService.updateCategory(userId, categoryId, categoryMap)
        return ResponseEntity<Map<String, Boolean>>(mapOf("success" to true), HttpStatus.OK)
    }

    @DeleteMapping("/{categoryId}")
    fun deleteCategory(request: HttpServletRequest, @PathVariable("categoryId") categoryId: Int)
            : ResponseEntity<Map<String, Boolean>> {
        val userId = request.getAttribute("userId") as Int
        categoryService.removeCategoryWithAllTransactions(userId, categoryId)
        return ResponseEntity<Map<String, Boolean>>(mapOf("success" to true), HttpStatus.OK)
    }
}