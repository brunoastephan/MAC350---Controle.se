package fb.controle.se.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import fb.controle.se.R
import fb.controle.se.database.DatabaseContract
import fb.controle.se.database.DbHelper
import fb.controle.se.database.DbReadController
import fb.controle.se.database.DbTransactionReader
import fb.controle.se.database.DbWriteController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        this.deleteDatabase(DbHelper.DATABASE_NAME)

        val writer = DbWriteController(this)
        writer.addCategory("teste", "t")
        writer.addCategory("sicrano", "s")


        writer.addTransaction("2024-05-17 23:19:32", 12.5F, 1)
        writer.addTransaction("2022-12-31 21:19:32", 33.5F, 2)
        writer.addTransaction("2023-04-17 19:23:32", 45.23F, 1)

        val readController = DbTransactionReader(this)

        Log.i("SQL", "transaction total: ${readController.readTransactionTotal()}")

        val transactionTotalView = findViewById<TextView>(R.id.TransactionTotalView)
        transactionTotalView.text = getString(R.string.transaction_total).format(readController.readTransactionTotal())

        supportActionBar?.hide()
    }
}