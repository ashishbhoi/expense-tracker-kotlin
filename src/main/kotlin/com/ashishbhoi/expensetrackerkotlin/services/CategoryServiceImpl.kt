package com.ashishbhoi.expensetrackerkotlin.services

import com.ashishbhoi.expensetrackerkotlin.exceptions.EtNotFoundException
import com.ashishbhoi.expensetrackerkotlin.models.Category
import com.ashishbhoi.expensetrackerkotlin.repositories.CategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl : CategoryService {

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    override fun fetchAllCategories(userId: Int): List<Category> {
        return categoryRepository.findAll(userId)
    }

    override fun fetchCategoryById(userId: Int, categoryId: Int): Category {
        return categoryRepository.findById(userId, categoryId) ?: throw EtNotFoundException("Category not found")
    }

    override fun addCategory(userId: Int, category: Category): Category {
        val categoryId = categoryRepository.create(userId, category.title, category.description)
        return categoryRepository.findById(userId, categoryId) ?: throw EtNotFoundException("Category not found")
    }

    override fun updateCategory(userId: Int, categoryId: Int, category: Category) {
        val count = categoryRepository.update(userId, categoryId, category)
        if (count.equals(0)) throw EtNotFoundException("Category not found")
    }

    override fun removeCategoryWithAllTransactions(userId: Int, categoryId: Int) {
        val count = categoryRepository.removeById(userId, categoryId)
        if (count.equals(0)) throw EtNotFoundException("Category not found")
    }
}