package fb.controle.se.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fb.controle.se.R
import fb.controle.se.database.DbHelper
import fb.controle.se.database.DbTransactionReader
import fb.controle.se.database.DbWriteController
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

enum class TrasactionViewState {
    DAY, MONTH, YEAR
}

class MainActivity : AppCompatActivity() {
    private lateinit var btnAddTrans : FloatingActionButton
    private lateinit var btnDay : Button
    private lateinit var btnMonth: Button
    private lateinit var btnYear : Button
    private lateinit var transactionTotalView : TextView

    private var TRANSACTION_VIEW_STATE = TrasactionViewState.YEAR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAddTrans = findViewById(R.id.addTransactionFloatingButton)
        btnAddTrans.setOnClickListener {
            val intent = Intent(this, NewExpenseActivity::class.java)
            startActivity(intent)
            finish()
        }

//        DbHelper(this).deleteDatabase()

        val writer = DbWriteController(this)
        writer.addCategory("DummyCat", "12")

        val readController = DbTransactionReader(this)

        val transactionsTotalDay : Float = readController.readTransactionsTotalFromIds(readController.readTransactionsInDay())
        val transactionsTotalMonth : Float = readController.readTransactionsTotalFromIds(readController.readTransactionsInMonth())
        val transactionsTotalYear : Float = readController.readTransactionsTotalFromIds(readController.readTransactionsInYear())

        btnDay = findViewById(R.id.visualizeDayButton)
        btnMonth = findViewById(R.id.visualizeMonthButton)
        btnYear = findViewById(R.id.visualizeYearButton)

        transactionTotalView = findViewById(R.id.TransactionTotalView)

        btnDay.setOnClickListener {
            TRANSACTION_VIEW_STATE = TrasactionViewState.DAY
            transactionTotalView.text = getString(R.string.transaction_total).format(transactionsTotalDay)
        }
        btnMonth.setOnClickListener {
            TRANSACTION_VIEW_STATE = TrasactionViewState.MONTH
            transactionTotalView.text = getString(R.string.transaction_total).format(transactionsTotalMonth)
        }
        btnYear.setOnClickListener {
            TRANSACTION_VIEW_STATE = TrasactionViewState.YEAR
            transactionTotalView.text = getString(R.string.transaction_total).format(transactionsTotalYear)
        }


//        val transactionsInRange = readController.readTransactionsInTimeInterval(LocalDateTime.of(2023, 12, 25, 6, 23), LocalDateTime.now())
//        Log.i("SQL", "transactions in datetime range: $transactionsInRange")
//        Log.i("SQL", "transaction total: ${readController.readTransactionsTotal()}")
//        Log.i("SQL", "transactions in day: ${readController.readTransactionsInDay()}")
//        Log.i("SQL", "transaction in month: ${readController.readTransactionsInMonth()}")
//        Log.i("SQL", "transaction in year: ${readController.readTransactionsInYear()}")
//

        supportActionBar?.hide()


    }
}