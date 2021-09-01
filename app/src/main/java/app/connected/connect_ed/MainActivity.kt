package app.connected.connect_ed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonFlashCards = findViewById(R.id.buttonFlashcards) as Button
        buttonFlashCards.setOnClickListener {
            val intent = Intent(this, FlashcardActivity::class.java)
            startActivity(intent)
        }

        val buttonWordOrder = findViewById(R.id.buttonWordOrder) as Button
        buttonWordOrder.setOnClickListener {
            val intent = Intent(this, WordOrderActivity::class.java)
            startActivity(intent)
        }
    }
}