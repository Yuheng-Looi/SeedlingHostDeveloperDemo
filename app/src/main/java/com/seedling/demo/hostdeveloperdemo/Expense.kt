package com.seedling.demo.hostdeveloperdemo

data class Expense(
    var amount: Float,
    val category: ExpenseCategory,
    val timestamp: Long,
    val dayTag: Int,
    val monthTag: Int,
    val yearTag: Int
)

enum class ExpenseCategory(val displayName: String) {
    GROCERY("Grocery"),
    SHOPPING("Shopping"),
    FOOD_DRINKS("Food & Drinks"),
    BILLS_FEES("Bills & Fees"),
    TRANSPORT("Transport"),
    GIFTS("Gifts"),
    BEAUTY("Beauty")
}
