package fb.controle.se.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import fb.controle.se.R
import fb.controle.se.database.DbWriteController
import java.text.SimpleDateFormat
import java.util.Date

class LoginActivity : AppCompatActivity() {
    private lateinit var mainButton: Button
    private lateinit var writer: DbWriteController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        writer = DbWriteController(this)
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val current = sdf.format(Date())

        writer.addCategory("feios", String(Character.toChars(0x1F349)))
        writer.addTransaction(current, 12.5F, 2)

        mainButton = findViewById(R.id.goMain)
        mainButton.setOnClickListener({
            val nextPage = Intent(this, MainActivity::class.java);
            startActivity(nextPage);
            finish();
        })


    }
}