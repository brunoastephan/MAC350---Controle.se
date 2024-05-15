package fb.controle.se.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fb.controle.se.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
    }
}