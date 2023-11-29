package com.seedling.demo.hostdeveloperdemo

class ExpenseManager {

    private val expenses: MutableList<Expense> = mutableListOf()

    fun addExpense(expense: Expense) {
        expenses.add(expense)
    }

    fun getExpensesForYear(year: Int): List<Expense> {
        return expenses.filter { it.yearTag == year }
    }

    fun getPieChartForYear(year: Int): List<Expense> {
        return calculatePieChart(expenses.filter { it.yearTag == year })
    }

    fun getExpensesForMonth(year: Int, month: Int): List<Expense> {
        return expenses.filter { it.yearTag == year && it.monthTag == month }
    }

    fun getPieChartForMonth(year: Int, month: Int): List<Expense> {
        return calculatePieChart(expenses.filter { it.yearTag == year && it.monthTag == month })
    }

    fun getExpensesForDay(year: Int, month: Int, day: Int): List<Expense> {
        return expenses.filter { it.yearTag == year && it.monthTag == month && it.dayTag == day }
    }

    fun getPieChartForDay(year: Int, month: Int, day: Int): List<Expense> {
        return calculatePieChart(expenses.filter { it.yearTag == year && it.monthTag == month && it.dayTag == day })
    }

    private fun calculatePieChart(filteredExpenses: List<Expense>): List<Expense> {
        val categoryTotalMap = mutableMapOf<ExpenseCategory, Float>()

        filteredExpenses.forEach { expense ->
            val totalAmount = categoryTotalMap.getOrDefault(expense.category, 0f)
            categoryTotalMap[expense.category] = totalAmount + expense.amount
        }

        // Convert the map back to a list of Expense objects
        return categoryTotalMap.map { (category, totalAmount) ->
            Expense(totalAmount, category, System.currentTimeMillis(), 0, 0, 0)
        }.sortedByDescending { it.amount }
    }
}