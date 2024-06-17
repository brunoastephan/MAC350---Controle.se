package fb.controle.se.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import fb.controle.se.R
import fb.controle.se.database.DbWriteController
import java.time.LocalDateTime

class NewCategoryActivity : AppCompatActivity() {

    private lateinit var newCatTextInput: EditText
    private lateinit var newIconTextInput: EditText

    private lateinit var dbWriteController: DbWriteController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_category)
        dbWriteController = DbWriteController(this)

        initializeButtons()

        newCatTextInput = findViewById(R.id.NewCatText)
        newIconTextInput = findViewById(R.id.NewIconText)

        supportActionBar?.hide()
    }

    private fun initializeButtons() {
        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener {
            backToMainActivity()
        }

        val btnSubmit = findViewById<Button>(R.id.btn_submit)
        btnSubmit.setOnClickListener {
            if (newCatTextInput.text.isNotEmpty() && newIconTextInput.text.isNotEmpty()) {
                dbWriteController.addCategory(newCatTextInput.text.toString(), newIconTextInput.text.toString())
                backToMainActivity()
            }
        }
    }

    private fun backToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}