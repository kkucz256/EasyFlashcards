package com.example.flashcards
import DatabaseHelper
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class DeckDao(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun insertDeck(deck: Deck): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("title", deck.title)
        }
        return db.insert("Decks", null, values)
    }

    fun getAllDecks(): List<Deck> {
        val db = dbHelper.readableDatabase
        val projection = arrayOf("deck_id", "title")
        val cursor = db.query("Decks", projection, null, null, null, null, null)
        val decks = mutableListOf<Deck>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow("deck_id"))
                val title = getString(getColumnIndexOrThrow("title"))
                decks.add(Deck(id, title))
            }
        }
        cursor.close()
        return decks
    }

    fun getDeckByName(deckName: String): Deck? {
        val db = dbHelper.readableDatabase
        val projection = arrayOf("deck_id", "title")
        val selection = "title = ?"
        val selectionArgs = arrayOf(deckName)
        val cursor = db.query("Decks", projection, selection, selectionArgs, null, null, null)
        var deck: Deck? = null
        with(cursor) {
            if (moveToFirst()) {
                val id = getInt(getColumnIndexOrThrow("deck_id"))
                val title = getString(getColumnIndexOrThrow("title"))
                deck = Deck(id, title)
            }
        }
        cursor.close()
        return deck
    }
}



