package app.connected.connect_ed.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.connected.connect_ed.R
import app.connected.connect_ed.callback.WordsDiffCallback

class WordsAdapter(private val onDragStarted: (String) -> Unit) : ListAdapter<String, WordsAdapter.WordsViewHolder>(
    WordsDiffCallback()
) {
    private var button: Button? = null // Probably a better way to do this logic
    private var sentenceAdapter: SentenceAdapter = SentenceAdapter(this)

    // Probably a better way to do this logic
    fun setConfirmButton(button: Button) {
        this.button = button
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)

        return WordsViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun removeItem(position: Int) {
        val list = ArrayList(currentList)
        list.removeAt(position)
        submitList(list)
    }

    fun addItem(selectedWord: String) {
        val list = ArrayList(currentList)
        list.add(selectedWord)
        submitList(list)
    }

    fun setSentenceAdapter(sentenceAdapter: SentenceAdapter) {
        sentenceAdapter.also { this.sentenceAdapter = it }
    }

    inner class WordsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(word: String) = itemView.run {
            findViewById<TextView>(R.id.tvWord).text = word

            setOnClickListener {
                // Probably a better way to do this logic
                button?.text = "Confirm"
                button?.setBackgroundColor(Color.rgb(52, 1, 93))
                button?.setTextColor(Color.WHITE)
                removeItem(adapterPosition)
                sentenceAdapter.addItem(word)
            }
        }
    }
}
