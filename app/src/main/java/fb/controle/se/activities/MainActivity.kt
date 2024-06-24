package fb.controle.se.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fb.controle.se.R
import fb.controle.se.database.DbCategoryReader
import fb.controle.se.database.DbTransactionReader
import fb.controle.se.database.DbWriteController
import fb.controle.se.utils.DynamicTimeBarChart
import fb.controle.se.utils.DynamicTimePieChart
import fb.controle.se.utils.TransactionViewState

class MainActivity : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences

    private lateinit var btnDay : Button
    private lateinit var btnMonth: Button
    private lateinit var btnYear : Button
    private lateinit var transactionTotalView : TextView
    private lateinit var transactionTotalViewIndicator : TextView

    private var transactionViewState = TransactionViewState.DAY

    private lateinit var fab : FloatingActionButton
    private lateinit var fab1 : FloatingActionButton
    private lateinit var fab2 : FloatingActionButton
    private lateinit var fabOpen : Animation
    private lateinit var fabClose : Animation
    private lateinit var rotateForward : Animation
    private lateinit var rotateBackward : Animation
    private var isOpen = false

    private lateinit var dbCategoryReader: DbCategoryReader
    private lateinit var dbTransactionReader: DbTransactionReader

    private lateinit var dynamicTimeBarChart: DynamicTimeBarChart
    private lateinit var dynamicTimePieChart: DynamicTimePieChart

    private fun animateFab() {
        if (isOpen) {
            fab.startAnimation(rotateBackward)
            fab1.startAnimation(fabClose)
            fab2.startAnimation(fabClose)
            fab1.isClickable = false
            fab2.isClickable = false
            isOpen = false
        } else {
            fab.startAnimation(rotateForward)
            fab1.startAnimation(fabOpen)
            fab2.startAnimation(fabOpen)
            fab1.isClickable = true
            fab2.isClickable = true
            isOpen = true
        }
    }

    private fun setupFloatingFabButton() {
        fab = findViewById(R.id.fab)
        fab1 = findViewById(R.id.fab1)
        fab2 = findViewById(R.id.fab2)

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)

        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward)

        fab.setOnClickListener {
            animateFab()
        }

        fab1.setOnClickListener {
            animateFab()
            val intent = Intent(this, NewExpenseActivity::class.java)
            startActivity(intent)
            finish()
        }

        fab2.setOnClickListener {
            animateFab()
            val intent = Intent(this, NewCategoryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupTransactionTotalView() {
        transactionTotalView = findViewById(R.id.TransactionTotalView)
        transactionTotalViewIndicator = findViewById(R.id.transactionTotalViewIndicator)

        btnDay = findViewById(R.id.visualizeDayButton)
        btnMonth = findViewById(R.id.visualizeMonthButton)
        btnYear = findViewById(R.id.visualizeYearButton)

        // no need to keep updating the transaction totals, since adding/removing transactions is in another activity
        val readController = DbTransactionReader(this)
        val transactionsTotalDay : Float = readController.readTransactionsTotalFromIds(readController.readTransactionsInDay())
        val transactionsTotalMonth : Float = readController.readTransactionsTotalFromIds(readController.readTransactionsInMonth())
        val transactionsTotalYear : Float = readController.readTransactionsTotalFromIds(readController.readTransactionsInYear())

        val transactionsTotalDayFormatted : String = getString(R.string.transaction_total).format(transactionsTotalDay)
        val transactionsTotalMonthFormatted : String = getString(R.string.transaction_total).format(transactionsTotalMonth)
        val transactionsTotalYearFormatted : String = getString(R.string.transaction_total).format(transactionsTotalYear)

        transactionTotalView.text = when(transactionViewState) {
            TransactionViewState.DAY -> transactionsTotalDayFormatted
            TransactionViewState.MONTH -> transactionsTotalMonthFormatted
            TransactionViewState.YEAR -> transactionsTotalYearFormatted
        }

        transactionTotalViewIndicator.text = when(transactionViewState) {
            TransactionViewState.DAY -> getString(R.string.day_button)
            TransactionViewState.MONTH -> getString(R.string.month_button)
            TransactionViewState.YEAR -> getString(R.string.year_button)

        }

        btnDay.setOnClickListener {
            transactionViewState = TransactionViewState.DAY
            transactionTotalView.text = transactionsTotalDayFormatted
            transactionTotalViewIndicator.text = getString(R.string.day_button)
            dynamicTimeBarChart.updateState(TransactionViewState.DAY)
            dynamicTimePieChart.updateState(TransactionViewState.DAY)
        }
        btnMonth.setOnClickListener {
            transactionViewState = TransactionViewState.MONTH
            transactionTotalView.text = transactionsTotalMonthFormatted
            transactionTotalViewIndicator.text = getString(R.string.month_button)
            dynamicTimeBarChart.updateState(TransactionViewState.MONTH)
            dynamicTimePieChart.updateState(TransactionViewState.MONTH)
        }
        btnYear.setOnClickListener {
            transactionViewState = TransactionViewState.YEAR
            transactionTotalView.text = transactionsTotalYearFormatted
            transactionTotalViewIndicator.text = getString(R.string.year_button)
            dynamicTimeBarChart.updateState(TransactionViewState.YEAR)
            dynamicTimePieChart.updateState(TransactionViewState.YEAR)
        }
    }

    private fun setupFirstTimeLogin() {
        Log.i("FIRST_LOGIN_INFO", "User first login registered")

        // Setup Initial Categories
        val writer = DbWriteController(this)

        val categoryFood = getString(R.string.category_food)
        val categoryTransport = getString(R.string.category_transport)
        val categoryLeisure = getString(R.string.category_leisure)
        val categoryFixedExpenses = getString(R.string.category_fixed_expenses)
        val categoryExtraCosts = getString(R.string.category_extra_costs)

        val categoryFoodIcon = getString(R.string.category_food_icon)
        val categoryTransportIcon = getString(R.string.category_transport_icon)
        val categoryLeisureIcon = getString(R.string.category_leisure_icon)
        val categoryFixedExpensesIcon = getString(R.string.category_fixed_expenses_icon)
        val categoryExtraCostsIcon = getString(R.string.category_extra_costs_icon)

        writer.addCategory(categoryFood, categoryFoodIcon)
        writer.addCategory(categoryTransport, categoryTransportIcon)
        writer.addCategory(categoryLeisure, categoryLeisureIcon)
        writer.addCategory(categoryFixedExpenses, categoryFixedExpensesIcon)
        writer.addCategory(categoryExtraCosts, categoryExtraCostsIcon)

        // save new first login state
        val editor = preferences.edit()

        editor.apply {
            putBoolean("firstTimeLogin", false)
            apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbCategoryReader = DbCategoryReader(this)
        dbTransactionReader = DbTransactionReader(this)

        preferences = getSharedPreferences("prefUserData", MODE_PRIVATE)
        val firstTimeLogin = preferences.getBoolean("firstTimeLogin", true)
        if (firstTimeLogin) setupFirstTimeLogin()

        val barChart : BarChart = findViewById(R.id.dummyBarChart)
        dynamicTimeBarChart = DynamicTimeBarChart(this, barChart, transactionViewState)

        val pieChart: PieChart = findViewById(R.id.dummyPieChart)
        dynamicTimePieChart = DynamicTimePieChart(this, pieChart, transactionViewState)

        setupTransactionTotalView()
        setupFloatingFabButton()

        supportActionBar?.hide()
    }
}