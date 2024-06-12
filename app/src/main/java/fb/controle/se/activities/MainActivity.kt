package fb.controle.se.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fb.controle.se.R
import fb.controle.se.database.DbHelper
import fb.controle.se.database.DbTransactionReader
import fb.controle.se.database.DbWriteController

enum class TransactionViewState {
    DAY, MONTH, YEAR
}

class MainActivity : AppCompatActivity() {
    private lateinit var btnAddTrans : FloatingActionButton
    private lateinit var btnDay : Button
    private lateinit var btnMonth: Button
    private lateinit var btnYear : Button
    private lateinit var transactionTotalView : TextView

    private var transactionViewState = TransactionViewState.YEAR

    private fun setupTransactionTotalView() {
        transactionTotalView = findViewById(R.id.TransactionTotalView)

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

        btnDay.setOnClickListener {
            transactionViewState = TransactionViewState.DAY
            transactionTotalView.text = transactionsTotalDayFormatted
        }
        btnMonth.setOnClickListener {
            transactionViewState = TransactionViewState.MONTH
            transactionTotalView.text = transactionsTotalMonthFormatted
        }
        btnYear.setOnClickListener {
            transactionViewState = TransactionViewState.YEAR
            transactionTotalView.text = transactionsTotalYearFormatted
        }
    }

    private fun setupTransactionFloatingButton() {
        btnAddTrans = findViewById(R.id.addTransactionFloatingButton)
        btnAddTrans.setOnClickListener {
            val intent = Intent(this, NewExpenseActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupTransactionTotalView()
        setupTransactionFloatingButton()

        supportActionBar?.hide()
    }
}