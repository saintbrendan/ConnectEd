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
        val buttons = listOf(
            findViewById<Button>(R.id.buttonA),
            findViewById<Button>(R.id.buttonB),
            findViewById<Button>(R.id.buttonC),
            findViewById<Button>(R.id.buttonD)
        )
        val buttonHome = findViewById<Button>(R.id.buttonHome)

        val imageList =
            intent.getIntArrayExtra(EXTRA_WORDLIST)?.toMutableList() ?: mutableListOf<Int>()
        val correctImage = imageList[0]
        val imagesFirstFour:MutableList<Int> = imageList.take(4) as MutableList<Int>
        imagesFirstFour.shuffle()

        val iCorrect: Int = imagesFirstFour.indexOf(correctImage)
        var text = "Which word best describes the image?"
        text = imagesFirstFour.toString() + correctImage.toString()
        textView.text = text

        buttons.forEachIndexed { index, button ->
            if (index == iCorrect) {
                button.setOnClickListener{
                    textView.text = "Correct!"
                }
            } else {
                button.setOnClickListener{
                    textView.text = "Nope"
                }
            }
            button.text = getResources().getResourceEntryName(imagesFirstFour[index])
        }

//        ButtonA.setOnClickListener {
//            val intent = Intent(this, FlashcardActivity::class.java).apply {
//                val first = imageList.removeFirst()
//                imageList.add(first)
//                putExtra(EXTRA_WORDLIST, imageList.toIntArray())
//            }
//            startActivity(intent)
//        }
//
//        buttonNo.setOnClickListener {
//            val intent = Intent(this, FlashcardActivity::class.java).apply {
//                val first = imageList.removeFirst()
//                imageList.add(2, first)
//                putExtra(EXTRA_WORDLIST, imageList.toIntArray())
//            }
//            startActivity(intent)
//        }

        buttonHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}