package com.example.flashcards

import CardDao
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StudyScreen : AppCompatActivity() {
    private var deckId = -1
    private var flashcards: MutableList<Card> = mutableListOf()
    private var allBacks: List<String> = listOf()
    private var score = 0
    private var counter = 0
    private lateinit var cardDao: CardDao
    private lateinit var scorefield: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.study_screen)
        val deckName = intent.getStringExtra("deck_name")
        deckId = intent.getIntExtra("deck_id", -1)

        cardDao = CardDao(this)
        flashcards = cardDao.getCardsByDeck(deckId).toMutableList()
        allBacks = flashcards.map { it.back }

        val textfield = findViewById<TextView>(R.id.current_text)
        textfield.setText("Currently studying:\n${deckName}")
        scorefield = findViewById<TextView>(R.id.score_text)
        updateScore()
        loadQuestion()

    }
    fun go_back(view: View) {
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }

                    DialogInterface.BUTTON_NEGATIVE -> {
                        dialog.dismiss()
                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to quit? \nCurrent progress will be lost")
            .setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener)
            .show()
    }

    fun loadQuestion() {
        if (flashcards.isEmpty()) {
            Toast.makeText(this, "No more flashcards!", Toast.LENGTH_SHORT).show()
            return
        }

        val flashcard = flashcards.random()
        val flashcardText = findViewById<TextView>(R.id.flashcard_text)
        flashcardText.setText(flashcard.front)

        val incorrectAnswers = allBacks.filter { it != flashcard.back }.shuffled().take(3).toMutableList()
        val answers = (incorrectAnswers + flashcard.back).shuffled()

        val buttons = listOf(
            findViewById<Button>(R.id.ans1),
            findViewById<Button>(R.id.ans2),
            findViewById<Button>(R.id.ans3),
            findViewById<Button>(R.id.ans4)
        )

        buttons.forEachIndexed { index, button ->
            button.text = answers[index]
            button.setOnClickListener {
                checkAnswer(button.text.toString(), flashcard)
            }
        }
    }


    fun checkAnswer(selectedAnswer: String, flashcard: Card) {
        if (selectedAnswer == flashcard.back) {
            if(flashcards.isNotEmpty()) {
                score++
                counter++
                updateScore()
            }
            flashcards.remove(flashcard)
        } else {
            if(flashcards.isNotEmpty()) {
                counter++
                updateScore()
            }
        }
        loadQuestion()
    }

    fun updateScore(){
        scorefield.setText("Current score: ${score}/${counter}")
    }

}