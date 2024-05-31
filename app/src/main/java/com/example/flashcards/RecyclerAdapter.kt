import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.Card
import com.example.flashcards.R

class RecyclerAdapter(private var flashcards: List<Card>, private val context: Context) : RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {

    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val frontTextView= view.findViewById<TextView>(R.id.flashcard_front)
        val backTextView = view.findViewById<TextView>(R.id.flashcard_back)
        val deleteButton = view.findViewById<Button>(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val flashcard = flashcards[position]
        holder.frontTextView.text = flashcard.front
        holder.backTextView.text = flashcard.back

        holder.deleteButton.setOnClickListener {
            val cardId = flashcard.card_id
            val mutableList = flashcards.toMutableList()
            mutableList.removeAt(position)
            flashcards = mutableList.toList()
            notifyItemRemoved(position)


            val cardDao = CardDao(context)
            cardDao.deleteCard(cardId)
        }
    }

    override fun getItemCount(): Int {
        return flashcards.size
    }
}