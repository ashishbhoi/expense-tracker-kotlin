package com.ashishbhoi.expensetrackerkotlin.repositories

import com.ashishbhoi.expensetrackerkotlin.exceptions.EtBadRequestException
import com.ashishbhoi.expensetrackerkotlin.exceptions.EtNotFoundException
import com.ashishbhoi.expensetrackerkotlin.models.Category

interface CategoryRepository {
    fun findAll(userId: Int): List<Category>

    @Throws(EtNotFoundException::class)
    fun findById(userId: Int, categoryId: Int): Category?

    @Throws(EtBadRequestException::class)
    fun create(userId: Int, title: String, description: String): Int

    @Throws(EtBadRequestException::class)
    fun update(userId: Int, categoryId: Int, category: Category)

    @Throws(EtNotFoundException::class)
    fun removeById(userId: Int, categoryId: Int)
}