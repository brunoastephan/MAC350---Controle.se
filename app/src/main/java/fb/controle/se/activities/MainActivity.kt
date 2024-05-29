package fb.controle.se.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fb.controle.se.R
import fb.controle.se.database.DatabaseContract
import fb.controle.se.database.DbHelper
import fb.controle.se.database.DbReadController
import fb.controle.se.database.DbTransactionReader
import fb.controle.se.database.DbWriteController
import java.time.LocalDate
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAddTrans = findViewById<FloatingActionButton>(R.id.addTransactionFloatingButton)
        btnAddTrans.setOnClickListener {
            val intent = Intent(this, NewExpenseActivity::class.java)
            startActivity(intent)
            finish()
        }

        this.deleteDatabase(DbHelper.DATABASE_NAME)

        val writer = DbWriteController(this)
        writer.addCategory("teste", "t")
        writer.addCategory("sicrano", "s")


        writer.addTransaction("2024-05-17 23:19:32", 12.5F, 1)
        writer.addTransaction("2022-12-31 21:19:32", 33.5F, 2)
        writer.addTransaction("2024-04-17 19:23:32", 45.23F, 1)
        writer.addTransaction("2025-04-17 19:23:32", 45.23F, 2)


        val readController = DbTransactionReader(this)

        val transactionsInRange = readController.readTransactionsInTimeInterval(LocalDateTime.of(2023, 12, 25, 6, 23), LocalDateTime.now())
        Log.i("SQL", "transactions in datetime range: $transactionsInRange")
        Log.i("SQL", "transaction total: ${readController.readTransactionsTotal()}")

        val transactionTotalView = findViewById<TextView>(R.id.TransactionTotalView)
        transactionTotalView.text = getString(R.string.transaction_total).format(readController.readTransactionsTotal())

        supportActionBar?.hide()


    }
}