package com.example.flashcards

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val deckDao = DeckDao(this)
        val spinner = findViewById<Spinner>(R.id.main_spinner)
        val decks = deckDao.getAllDecks()
        val adapter = ArrayAdapter(this, R.layout.custom_spinner_item, decks.map { it.title })
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        spinner.adapter = adapter

    }
    fun create_btn(view: View) {
        val intent = Intent(this, CreateScreen::class.java)
        startActivity(intent)
    }
    fun study_btn(view: View){
        val spinner = findViewById<Spinner>(R.id.main_spinner)
        val selectedDeckName = spinner.selectedItem.toString()
        val deckDao = DeckDao(this)
        val selectedDeck = deckDao.getDeckByName(selectedDeckName)

        if (selectedDeck != null) {
            val intent = Intent(this, StudyScreen::class.java)
            intent.putExtra("deck_name", selectedDeck.title)
            intent.putExtra("deck_id", selectedDeck.id)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Failed to find the selected deck", Toast.LENGTH_SHORT).show()
        }
    }
}