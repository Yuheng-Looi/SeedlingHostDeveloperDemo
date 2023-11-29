package com.seedling.demo.hostdeveloperdemo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.formatter.ValueFormatter

class ReportActivity : AppCompatActivity() {

    private lateinit var pieChart: PieChart
    private val expManager = ExpenseManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        pieChart = findViewById(R.id.pie_chart)

        randomExpenses() // generate random expenses

        val monthlyExpenses = expManager.getPieChartForMonth(2023,11)
        updatePieChart(monthlyExpenses)

        findViewById<Button>(R.id.btnDaily).setOnClickListener {
            val dailyExpenses = expManager.getPieChartForDay(2023,11, 29)
            updatePieChart(dailyExpenses)
        }

        findViewById<Button>(R.id.btnMonthly).setOnClickListener {
            updatePieChart(monthlyExpenses)
        }

        findViewById<Button>(R.id.btnYearly).setOnClickListener {
            val yearlyExpenses = expManager.getPieChartForYear(2023)
            updatePieChart(yearlyExpenses)
        }

    }

    private fun randomExpenses() {
        expManager.addExpense(Expense(50f, ExpenseCategory.BEAUTY, System.currentTimeMillis(), 29, 11, 2023))
        expManager.addExpense(Expense(30f, ExpenseCategory.FOOD_DRINKS, System.currentTimeMillis(), 29, 11, 2023))
        expManager.addExpense(Expense(20f, ExpenseCategory.FOOD_DRINKS, System.currentTimeMillis(), 29, 11, 2023))
        expManager.addExpense(Expense(300f, ExpenseCategory.FOOD_DRINKS, System.currentTimeMillis(), 28, 11, 2023))
        expManager.addExpense(Expense(250F, ExpenseCategory.GIFTS, System.currentTimeMillis(), 27, 11, 2023))
        expManager.addExpense(Expense(50f, ExpenseCategory.BEAUTY, System.currentTimeMillis(), 26, 11, 2023))
        expManager.addExpense(Expense(250f, ExpenseCategory.SHOPPING, System.currentTimeMillis(), 29, 1, 2023))
        expManager.addExpense(Expense(250f, ExpenseCategory.GROCERY, System.currentTimeMillis(), 29, 10, 2023))
        expManager.addExpense(Expense(150f, ExpenseCategory.FOOD_DRINKS, System.currentTimeMillis(), 29, 9, 2023))
        expManager.addExpense(Expense(450f, ExpenseCategory.BILLS_FEES, System.currentTimeMillis(), 29, 6, 2023))
        expManager.addExpense(Expense(650f, ExpenseCategory.TRANSPORT, System.currentTimeMillis(), 29, 11, 2023))
    }

    private fun updatePieChart(expenses: List<Expense>) {
        val list: ArrayList<PieEntry> = ArrayList()

        for (expense in expenses) {
            list.add(PieEntry(expense.amount, expense.category.displayName))
        }

        val colors = intArrayOf(
            Color.rgb(255, 102, 102), // Red
            Color.rgb(255, 204, 102), // Orange
            Color.rgb(255, 255, 102), // Yellow
            Color.rgb(102, 255, 102), // Green
            Color.rgb(102, 255, 255), // Cyan
            Color.rgb(102, 102, 255), // Blue
            Color.rgb(204, 102, 255)  // Purple
        )

        val pieDataSet = PieDataSet(list, "Categories")
        pieDataSet.setColors(colors, 255)
        pieDataSet.valueTextSize = 15f
        pieDataSet.valueTextColor = Color.WHITE
        pieDataSet.sliceSpace = 1f
        pieDataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE


        val pieData = PieData(pieDataSet)
        pieData.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return String.format("%.1f%%", value)
            }
        })

        pieChart.data = pieData
        pieChart.setHoleColor(Color.BLACK)
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false // TODO: LET USER CHOOSE IF GOT TIME, ALONG WITH PIE CHART XVALUE
        pieChart.animateY(500)
    }

    private fun showPercentage() {
        // TODO: SHOW THE PIE CHART VALUE IN PERCENTAGE
    }

    private fun showAmount() {
        // TODO: SHOW THE PIE CHART VALUE IN AMOUNT
    }
}