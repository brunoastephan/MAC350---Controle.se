package fb.controle.se

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    private lateinit var mainButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        mainButton = findViewById(R.id.goMain)
        mainButton.setOnClickListener({
            val nextPage = Intent(this, MainActivity::class.java);
            startActivity(nextPage);
            finish();
        })

    }
}