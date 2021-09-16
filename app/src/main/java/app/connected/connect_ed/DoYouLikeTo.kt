package app.connected.connect_ed

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat


class DoYouLikeTo : AppCompatActivity() {
    var imageList = mutableListOf<Int>(
        R.drawable.rest,
        R.drawable.climb,
        R.drawable.argue,
        R.drawable.cry,
        R.drawable.dance,
        R.drawable.dive,
        R.drawable.exercise,
        R.drawable.windsurf,
        R.drawable.walk,
        R.drawable.write,
        R.drawable.cook,
    )
    var imgIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_do_you_like_to)
        var correctAnswer = "walk"

        val inputText = findViewById<EditText>(R.id.editTextTextPersonName);
        val imgView = findViewById<ImageView>(R.id.imageView2)
        val buttonCorrect = findViewById<Button>(R.id.buttonCorrect)
        val buttonHome = findViewById<Button>(R.id.buttonHome2)

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
                    inputText.setTextColor(Color.GREEN)
                    buttonCorrect.visibility = View.VISIBLE
                }
            }
        })

        buttonCorrect.setOnClickListener {
            buttonCorrect.visibility = View.INVISIBLE
            inputText.text.clear()
            imgIndex = (imgIndex + 1) % imageList.size
            correctAnswer = getResources().getResourceName(imageList[imgIndex]).substringAfterLast("/")
            inputText.setTextColor(Color.BLACK)
            val d = ResourcesCompat.getDrawable(resources, imageList[imgIndex], null)
            imgView.setImageDrawable(d)
        }

        buttonHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}