package com.example.flashcards

import CardDao
import RecyclerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FlashcardsAdding : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var cardDao: CardDao
    private var deckId = -1
    private var flashcards: MutableList<Card> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flashcard_adding)
        val deckName = intent.getStringExtra("deck_name")
        deckId = intent.getIntExtra("deck_id", -1)

        cardDao = CardDao(this)
        flashcards = cardDao.getCardsByDeck(deckId).toMutableList()

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = RecyclerAdapter(flashcards, this@FlashcardsAdding)
        recyclerView.adapter = recyclerAdapter

        val deckText = findViewById<TextView>(R.id.deck_text)
        deckText.text = "Working on: \n${deckName}"


    }
    fun go_back(view: View){
        val intent = Intent(this, CreateScreen::class.java)
        startActivity(intent)
    }

    fun add_card(view: View) {
        val frontText = findViewById<EditText>(R.id.fronttext).text.toString()
        val backText = findViewById<EditText>(R.id.backtext).text.toString()

        if (frontText.isNotEmpty() && backText.isNotEmpty()) {
            val newCard = Card(0,deckId, frontText, backText)
            cardDao.insertCard(newCard)
            flashcards.add(newCard)
            recyclerAdapter.notifyItemInserted(flashcards.size - 1)

            findViewById<EditText>(R.id.fronttext).text.clear()
            findViewById<EditText>(R.id.backtext).text.clear()
        } else {

            Toast.makeText(this, "Please fill in both front and back text", Toast.LENGTH_SHORT).show()
        }
    }
}