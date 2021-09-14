package app.connected.connect_ed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


class DoYouLikeTo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_do_you_like_to)
        val correctAnswer = "walk"

        val inputText = findViewById<EditText>(R.id.editTextTextPersonName);

        inputText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val t = inputText.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val t = inputText.text.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val t = inputText.text.toString()
                if (inputText.text.toString() == correctAnswer) {
                    inputText.setTextColor(0xFF0000)
                }
            }
        })
    }
}