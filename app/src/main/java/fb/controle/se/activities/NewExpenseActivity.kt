package fb.controle.se.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fb.controle.se.R

class NewExpenseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_expense)

        supportActionBar?.hide()
    }
}