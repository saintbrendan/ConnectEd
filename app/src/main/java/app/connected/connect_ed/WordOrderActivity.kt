package app.connected.connect_ed

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import app.connected.connect_ed.adapter.SentenceAdapter
import app.connected.connect_ed.adapter.WordsAdapter
import app.connected.connect_ed.databinding.ActivityWordOrderBinding
import com.google.android.flexbox.*

class WordOrderActivity : AppCompatActivity() {
    private val binding by lazy { ActivityWordOrderBinding.inflate(layoutInflater) }
    var sentenceList = mutableListOf<Int>(
        R.array.sentence1,
        R.array.sentence2,
        R.array.sentence3,
        R.array.sentence4,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var iSentence = 0

        var sentences = mutableListOf<MutableList<String>>()
        for (sentence in sentenceList) {
            val wordArray4 = resources.getStringArray(sentence)
            sentences.add(mutableListOf<String>(*wordArray4))
        }

        val wordsAdapter = WordsAdapter {
        }.apply {
            var shuffledSentence = sentences[iSentence].toMutableList()
            shuffledSentence.shuffle()
            submitList(shuffledSentence)
        }
        val sentenceAdapter = SentenceAdapter(wordsAdapter)

        binding.rvSentence.layoutManager =
            FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP).apply {
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.CENTER
            }
        binding.rvSentence.adapter = sentenceAdapter

        binding.rvWords.layoutManager =
            FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP).apply {
                justifyContent = JustifyContent.FLEX_START
                alignItems = AlignItems.CENTER
            }

        binding.rvWords.adapter = wordsAdapter

        val button = findViewById<Button>(R.id.button2)
        button.setOnClickListener {
            if (sentenceAdapter.currentList == sentences[iSentence]) {
                if (button.text == "Confirm") {
                    button.setBackgroundColor(Color.GREEN)
                    button.text = "Continue"
                    button.setTextColor(Color.BLACK)
                } else {
                    button.setBackgroundColor(Color.rgb(98, 0, 238))
                    button.setTextColor(Color.WHITE)
                    button.text = "Confirm"
                    iSentence = (iSentence + 1) % 4
                    var shuffledSentence = sentences[iSentence].toMutableList()
                    shuffledSentence.shuffle()
                    wordsAdapter.submitList(shuffledSentence)
                    sentenceAdapter.clear()
                }

            } else {
                button.setBackgroundColor(Color.RED)
                button.text = "Try again"
            }
        }
        val buttonHome = findViewById<Button>(R.id.buttonHome)
        buttonHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}