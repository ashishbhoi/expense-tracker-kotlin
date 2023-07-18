package com.ashishbhoi.expensetrackerkotlin.services

import com.ashishbhoi.expensetrackerkotlin.exceptions.EtBadRequestException
import com.ashishbhoi.expensetrackerkotlin.exceptions.EtNotFoundException
import com.ashishbhoi.expensetrackerkotlin.models.Category

interface CategoryService {
    fun fetchAllCategories(userId: Int): List<Category>

    @Throws(EtNotFoundException::class)
    fun fetchCategoryById(userId: Int, categoryId: Int): Category

    @Throws(EtBadRequestException::class)
    fun addCategory(userId: Int, category: Category): Category

    @Throws(EtBadRequestException::class)
    fun updateCategory(userId: Int, categoryId: Int, category: Category)

    @Throws(EtNotFoundException::class)
    fun removeCategoryWithAllTransactions(userId: Int, categoryId: Int)
}