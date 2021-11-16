package app.connected.connect_ed

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import java.util.*

const val EXTRA_WORDLIST = "com.example.mergeded.WORDLIST"
const val EXTRA_SOUNDLIST = "com.example.mergeded.SOUNDLIST"

class FlashcardActivity : AppCompatActivity() {
    var soundList = mutableListOf<Int>(
        R.raw.rest,
        R.raw.climb,
        R.raw.argue,
        R.raw.cry,
        R.raw.dance,
        R.raw.dive,
        R.raw.exercise,
        R.raw.windsurf,
        R.raw.walk,
        R.raw.write,
        R.raw.cook,
    )
    var verb:String = ""

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard)

        val imageUserListens = findViewById<ImageView>(R.id.imageUserListens);
        val imageUserSpeaks = findViewById<ImageView>(R.id.imageUserSpeaks);
        val textRight = findViewById<TextView>(R.id.textRight);

        fun getDrawablesList(): List<Int> {
            val drawableClass = R.drawable::class
            val instance = drawableClass.constructors.first().call()
            val drawablesList = mutableListOf<Int>()
            for (field in instance.javaClass.declaredFields) {
                val test = field.getInt(instance)
                val word = resources.getResourceEntryName(test)
                if (word.startsWith("image_", ignoreCase = false)) {
                    drawablesList.add(test)
                }
            }
            return drawablesList
        }

        var newList = getDrawablesList()

        val imageView = findViewById<ImageView>(R.id.imageView)
        val imageArray = intent.getIntArrayExtra(EXTRA_WORDLIST)
        val soundArray = intent.getIntArrayExtra(EXTRA_SOUNDLIST)
        if (imageArray != null) {
            newList = imageArray.toMutableList()
        }
        if (soundArray != null) {
            soundList = soundArray.toMutableList()
        }

        mediaPlayer = MediaPlayer.create(this, soundList[0])
        mediaPlayer?.setOnPreparedListener {
        }
        mediaPlayer?.start()

        verb = getResources().getResourceEntryName(newList[0])
        verb = verb.removePrefix("image_").replace("_", " ")


        //imageView.setImageResource(imageList[0])
        val displayMetrics: DisplayMetrics = getResources().getDisplayMetrics()
        Glide.with(this).load(
            newList[0]
        ).override(displayMetrics.widthPixels, displayMetrics.heightPixels).into(imageView)

        fun getSpeechInput() {
            val intent = Intent(
                RecognizerIntent
                    .ACTION_RECOGNIZE_SPEECH
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )

            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, 10)
            } else {
                Toast.makeText(
                    this,
                    "Your Device Doesn't Support Speech Input",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        imageUserSpeaks.setOnClickListener(View.OnClickListener {
            getSpeechInput()
        })

        imageUserListens.setOnClickListener {
            mediaPlayer?.start()
        }

        imageView.setOnClickListener {
            val intent = Intent(this, RightOrWrongActivity::class.java).apply {
                putExtra(EXTRA_WORDLIST, newList.toIntArray())
                putExtra(EXTRA_SOUNDLIST, soundList.toIntArray())
            }
            startActivity(intent)
        }
        textRight.setOnClickListener {
            val intent = Intent(this, RightOrWrongActivity::class.java).apply {
                putExtra(EXTRA_WORDLIST, newList.toIntArray())
                putExtra(EXTRA_SOUNDLIST, soundList.toIntArray())
            }
            startActivity(intent)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode, data
        )
        val textResult = findViewById<TextView>(R.id.textResult);

        when (requestCode) {
            10 -> if (resultCode == RESULT_OK &&
                data != null
            ) {
                val result =
                    data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS
                    )
                val spokenText = result?.get(0)?.lowercase()
                var msg = ""
                if (spokenText == verb.lowercase() || spokenText == "to "+verb.lowercase()) {
                    msg = "Congratulations!  Correct!  \nClick the right arrow to continue."
                } else {
                    msg = "you said " + spokenText + " and we were expecting " + verb
                }
                textResult.text = msg
            }
        }
    }

}