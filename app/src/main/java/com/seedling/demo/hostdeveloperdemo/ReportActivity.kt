package com.seedling.demo.hostdeveloperdemo

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.formatter.ValueFormatter

class ReportActivity : AppCompatActivity() {

    private lateinit var pieChart: PieChart
    private val expManager = ExpenseManager()
    private lateinit var transactions: String

    private val year = 2023
    private val month = 11
    private val day = 29
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        pieChart = findViewById(R.id.pie_chart)

        randomExpenses() // generate random expenses

        val monthlyExpenses = expManager.getPieChartForMonth(year, month)
        val monthlyTransaction = expManager.getExpensesForMonth(year, month)
        updatePieChart(monthlyExpenses)
        handleTransaction(monthlyTransaction)

        findViewById<Button>(R.id.btnDaily).setOnClickListener {
            val dailyExpenses = expManager.getPieChartForDay(year, month, day)
            updatePieChart(dailyExpenses)

            val dailyTransaction = expManager.getExpensesForDay(year, month, day)
            handleTransaction(dailyTransaction)
        }

        findViewById<Button>(R.id.btnMonthly).setOnClickListener {
            updatePieChart(monthlyExpenses)
            handleTransaction(monthlyTransaction)
        }

        findViewById<Button>(R.id.btnYearly).setOnClickListener {
            val yearlyExpenses = expManager.getPieChartForYear(year)
            updatePieChart(yearlyExpenses)

            val yearlyTransaction = expManager.getExpensesForYear(year)
            handleTransaction(yearlyTransaction)

        }

        TransactionHistoryOnClick()

        initClickBills()

    }

    private fun initClickBills() {
        val bta = BankTransferActivity()

        findViewById<ImageButton>(R.id.ibElectricBill).setOnClickListener {
            val intent = Intent(this, BankTransferActivity::class.java)
//            bta.setTitle("Electric Bill")
            startActivity(intent)
        }
        findViewById<ImageButton>(R.id.ibWaterBill).setOnClickListener {
            val intent = Intent(this, BankTransferActivity::class.java)
//            bta.setTitle("Water Bill")
            startActivity(intent)
        }
        findViewById<ImageButton>(R.id.ibWifi).setOnClickListener {
            val intent = Intent(this, BankTransferActivity::class.java)
//            bta.setTitle("Wifi Bill")
            startActivity(intent)
        }
        findViewById<ImageButton>(R.id.ibPostpaid).setOnClickListener {
            val intent = Intent(this, BankTransferActivity::class.java)
//            bta.setTitle("Postpaid")
            startActivity(intent)
        }
        findViewById<ImageButton>(R.id.ibPrepaid).setOnClickListener {
            val intent = Intent(this, BankTransferActivity::class.java)
//            bta.setTitle("Prepaid")
            startActivity(intent)
        }
        findViewById<ImageButton>(R.id.ibParking).setOnClickListener {
            val intent = Intent(this, BankTransferActivity::class.java)
//            bta.setTitle("Parking")
            startActivity(intent)
        }
    }

    private fun handleTransaction(transactions: List<Expense>) {

        val transactionItemList = transactions.map { expense ->
            TransactionItem(
                "${expense.dayTag}-${expense.monthTag}-${expense.yearTag}\n${expense.category}",
                "RM${String.format("%.2f",expense.amount)}"
            )
        }

        val recyclerView: RecyclerView = findViewById(R.id.rvTransaction)
        recyclerView.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TransactionAdapter(transactionItemList)

        var totalexpenses = 0.00f
        for (expense in transactions) {
            totalexpenses += expense.amount
        }
        val formattedTotalExpenses = String.format("%.2f", totalexpenses)

        val totalExpense = findViewById<TextView>(R.id.tvTotalExpense)
        totalExpense.text = "Total Expenses: RM$formattedTotalExpenses"

    }

    private fun TransactionHistoryOnClick() {
        val ivArrow = findViewById<ImageView>(R.id.ivArrow)
        val rvTransaction = findViewById<RecyclerView>(R.id.rvTransaction)
        ivArrow.setOnClickListener {
            if (rvTransaction.visibility == View.GONE) {
                rvTransaction.visibility = View.VISIBLE
                ivArrow.setImageResource(R.drawable.ic_arrow_down)
            } else {
                rvTransaction.visibility = View.GONE
                ivArrow.setImageResource(R.drawable.ic_arrow_left)
            }
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
        expManager.addExpense(Expense(650f, ExpenseCategory.TRANSPORT, System.currentTimeMillis(), 20, 11, 2023))
    }

    private fun updatePieChart(expenses: List<Expense>) {
        val list: ArrayList<PieEntry> = ArrayList()

        for (expense in expenses) {
            list.add(PieEntry(expense.amount, expense.category.displayName))
        }

        val colors = intArrayOf(
            Color.rgb(255, 102, 102), // Red
            Color.rgb(255, 204, 102), // Orange
            Color.rgb(255, 235, 102), // Yellow
            Color.rgb(102, 240, 102), // Green
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

    // TODO: CREATE MOCK INTERFACE FOR PAY BILLS
}