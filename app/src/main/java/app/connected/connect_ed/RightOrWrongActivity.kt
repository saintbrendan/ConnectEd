package app.connected.connect_ed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class RightOrWrongActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_right_or_wrong)

        val textView = findViewById<TextView>(R.id.textView)
        val buttonYes = findViewById<Button>(R.id.buttonYes)
        val buttonNo = findViewById<Button>(R.id.buttonNo)
        val buttonHome = findViewById<Button>(R.id.buttonHome)

        val imageList = intent.getIntArrayExtra(EXTRA_WORDLIST)?.toMutableList() ?: mutableListOf<Int>()
        val word = getResources().getResourceEntryName(imageList[0]);

        val text = "Did you guess \"to ${word}?\""
        textView.text = text

        buttonYes.setOnClickListener {
            val intent = Intent(this, FlashcardActivity::class.java).apply {
                val first = imageList.removeFirst()
                imageList.add(first)
                putExtra(EXTRA_WORDLIST, imageList.toIntArray())
            }
            startActivity(intent)
        }

        buttonNo.setOnClickListener {
            val intent = Intent(this, FlashcardActivity::class.java).apply {
                val first = imageList.removeFirst()
                imageList.add(2, first)
                putExtra(EXTRA_WORDLIST, imageList.toIntArray())
            }
            startActivity(intent)
        }

        buttonHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}