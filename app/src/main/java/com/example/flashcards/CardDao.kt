import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.flashcards.Card

class CardDao(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun insertCard(card: Card): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("deck_id", card.deck_id)
            put("front", card.front)
            put("back", card.back)
        }
        return db.insert("Cards", null, values)
    }

    fun getCardsByDeck(deckId: Int): List<Card> {
        val db = dbHelper.readableDatabase
        val projection = arrayOf("card_id", "deck_id", "front", "back")
        val selection = "deck_id = ?"
        val selectionArgs = arrayOf(deckId.toString())
        val cursor = db.query("Cards", projection, selection, selectionArgs, null, null, null)
        val cards = mutableListOf<Card>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("card_id"))
                val deckId = getInt(getColumnIndexOrThrow("deck_id"))
                val front = getString(getColumnIndexOrThrow("front"))
                val back = getString(getColumnIndexOrThrow("back"))
                cards.add(Card(id,deckId, front, back))
            }
        }
        cursor.close()
        return cards
    }
    fun deleteCard(cardId: Int): Int {
        val db = dbHelper.writableDatabase
        val selection = "card_id = ?"
        val selectionArgs = arrayOf(cardId.toString())
        return db.delete("Cards", selection, selectionArgs)
    }
}
