package fb.controle.se.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.Button
import android.widget.TextView
import fb.controle.se.R
import fb.controle.se.database.DbWriteController
import java.time.LocalDateTime

class NewExpenseActivity : AppCompatActivity() {

    private var strNumber = StringBuilder()
    private lateinit var display: TextView
    private lateinit var numberButtons: Array<Button>

    private lateinit var dbWriteController: DbWriteController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_expense)
        dbWriteController = DbWriteController(this)

        dbWriteController.addCategory("teste", "1")
        dbWriteController.addCategory("sicrano", ":)")

        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener {
            backToMainActivity()
        }

        val btnSubmit = findViewById<Button>(R.id.btn_submit)
        btnSubmit.setOnClickListener {
            val submitValue: Float = display.text.toString().toFloat()
            dbWriteController.addTransaction(LocalDateTime.now(), submitValue, 1)
            backToMainActivity()
        }

        initializeComponents()

        supportActionBar?.hide()
    }

    private fun backToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initializeComponents() {
        display = findViewById(R.id.newTransactionTotalView)

        //Numbers

        val btn0:Button = findViewById(R.id.btn_0)
        val btn1:Button = findViewById(R.id.btn_1)
        val btn2:Button = findViewById(R.id.btn_2)
        val btn3:Button = findViewById(R.id.btn_3)
        val btn4:Button = findViewById(R.id.btn_4)
        val btn5:Button = findViewById(R.id.btn_5)
        val btn6:Button = findViewById(R.id.btn_6)
        val btn7:Button = findViewById(R.id.btn_7)
        val btn8:Button = findViewById(R.id.btn_8)
        val btn9:Button = findViewById(R.id.btn_9)
        val btn00:Button = findViewById(R.id.btn_00)

        //Clear

        val btnDel:Button = findViewById((R.id.btn_del))
        btnDel.setOnClickListener{buttonDelClick()}

        val btnClear:Button = findViewById((R.id.btn_clear))
        btnClear.setOnClickListener{buttonClearClick()}

        numberButtons = arrayOf(btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn00)
        for(i in numberButtons){
            i.setOnClickListener {numberButtonClicked(i)}
        }
    }

    private fun buttonClearClick() {
        strNumber.clear()
        updateDisplay()
    }

    private fun buttonDelClick() {
        if (strNumber.isNotEmpty()) strNumber.setLength(strNumber.length - 1)
        updateDisplay()
    }

    private fun numberButtonClicked(btn: Button) {
        strNumber.append(btn.text)
        updateDisplay()
    }

    private fun updateDisplay() {
        if (strNumber.isNotEmpty()) {
            val aux1 = (strNumber.toString().toFloat())/100
            var aux2 = aux1.toString()
            if (aux2.substring(aux2.length-2, aux2.length-1) == ".") aux2 += "0"
            display.text = aux2
        }
        else {
            display.text = strNumber
        }
    }
}
