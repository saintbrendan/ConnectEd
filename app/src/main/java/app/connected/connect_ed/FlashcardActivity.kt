package app.connected.connect_ed

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide

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

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard)

        val imageUserListens = findViewById<ImageView>(R.id.imageUserListens);

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

        //imageView.setImageResource(imageList[0])
        val displayMetrics: DisplayMetrics = getResources().getDisplayMetrics()
        Glide.with(this).load(
            newList[0]
        ).override(displayMetrics.widthPixels, displayMetrics.heightPixels).into(imageView)

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
    }

}