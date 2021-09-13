package app.connected.connect_ed

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ImageView
import com.bumptech.glide.Glide

const val EXTRA_WORDLIST = "com.example.mergeded.WORDLIST"
const val EXTRA_SOUNDLIST = "com.example.mergeded.SOUNDLIST"

class FlashcardActivity : AppCompatActivity() {
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


        val imageView = findViewById<ImageView>(R.id.imageView)
        val imageArray = intent.getIntArrayExtra(EXTRA_WORDLIST)
        val soundArray = intent.getIntArrayExtra(EXTRA_SOUNDLIST)
        if (imageArray != null) {
            imageList = imageArray.toMutableList()
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
            imageList[0]
        ).override(displayMetrics.widthPixels, displayMetrics.heightPixels).into(imageView)

        imageView.setOnClickListener {


            val intent = Intent(this, RightOrWrongActivity::class.java).apply {
                putExtra(EXTRA_WORDLIST, imageList.toIntArray())
            }
            startActivity(intent)
        }
    }
}