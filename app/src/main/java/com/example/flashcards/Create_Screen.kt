package com.example.flashcards
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CreateScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_screen)
        val deckDao = DeckDao(this)

        val spinner = findViewById<Spinner>(R.id.spinner)
        val decks = deckDao.getAllDecks()
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_item, decks.map { it.title })
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    fun go_back(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    fun create_set(view: View) {
        val deckNameEditText = findViewById<EditText>(R.id.deck_name_text)
        val deckDao = DeckDao(this)

        val deckName = deckNameEditText.text.toString()
        if (deckName.isNotEmpty()) {
            val newDeckId = deckDao.insertDeck(Deck(0,deckName))
            val intent = Intent(this, FlashcardsAdding::class.java)
            intent.putExtra("deck_name", deckName)
            intent.putExtra("deck_id", newDeckId)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Please enter a deck name", Toast.LENGTH_SHORT).show()
        }
    }
    fun edit_set(view: View) {
        val spinner = findViewById<Spinner>(R.id.spinner)
        val selectedDeckName = spinner.selectedItem.toString()

        val deckDao = DeckDao(this)
        val selectedDeck = deckDao.getDeckByName(selectedDeckName)

        if (selectedDeck != null) {
            val intent = Intent(this, FlashcardsAdding::class.java)
            intent.putExtra("deck_name", selectedDeck.title)
            intent.putExtra("deck_id", selectedDeck.id)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Failed to find the selected deck", Toast.LENGTH_SHORT).show()
        }
    }
}