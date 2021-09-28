package com.example.alkhurayyif_linah_guessthephrase

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val gameAnswer = "Squid Game"
    var gameAnswerhash = ""
    var guessedLetters = ""
    var isguessPhrase = true
    var GuessCount = 0
    lateinit var userguess:EditText
    lateinit var messages:ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        check_lettersPhrase(' ')
        userguess = findViewById(R.id.userGuess)
        updateAnswer()
        messages = ArrayList()

        Guessbutton.setOnClickListener{
          checkAnswer()
            recyclerView.adapter = GuessthephraseAdapter(messages)
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
    }
    fun checkAnswer(){
        val user_Guess = userGuess.text.toString()
        if(isguessPhrase){
            if(user_Guess.toUpperCase() == gameAnswer.toUpperCase()){
                Alert("You win!")
            }else{
//                Alert("You Lost!")
                messages.add("Wrong guess: $user_Guess")
                isguessPhrase = false
            }
        }else{
            if(user_Guess.isNotEmpty() && user_Guess.length==1){
                isguessPhrase = true
                checkAnswer_letters(user_Guess)
            }else{
                Snackbar.make(mainLayout, "Please enter only one letter!!", Snackbar.LENGTH_LONG).show()
            }
        }

         userguess.text.clear()
    }
    fun checkAnswer_letters(letter:String ){
        var numberfund = 0
        GuessCount++
        var guessesLeft = 10 - GuessCount
        var letter_char:Char=' '
        for(char in gameAnswer.indices){
            if(gameAnswer[char].toString().toUpperCase()
                == letter.toUpperCase()){
                numberfund++
            }
        }
        if(numberfund>0){
            messages.add("Find $numberfund ${letter.toUpperCase()}(s)")
        }else{
            messages.add("No ${letter.toUpperCase()} is found")
        }

        if(GuessCount<10){messages.add("$guessesLeft guesses remaining")}

        guessedLetters = letter
        letter_char = letter.single()
       check_lettersPhrase(letter_char.toUpperCase())
        updateAnswer()
    }
    fun check_lettersPhrase(letters:Char){
     var gameAnswerhash_new:String = gameAnswerhash
        gameAnswerhash=""
       if(letters==' '){
           for(char in gameAnswer.indices){
               if(gameAnswer[char] == ' '){
                   gameAnswerhash +=' '
               }else{
                   gameAnswerhash +='*'
               }
           }
       }else{
           for(char in gameAnswer.indices){
               if(gameAnswer[char].toUpperCase() == ' '){
                   gameAnswerhash +=' '
               }else if(gameAnswer[char].toUpperCase() == letters){
                   gameAnswerhash += "$letters"

               }else{
                   if(gameAnswerhash_new[char].toUpperCase() != '*'){
                       gameAnswerhash += gameAnswerhash_new[char]
                   }else{
                       gameAnswerhash +='*'
                   }

               }
           }
       }
    }
    fun updateAnswer(){
        PhrasetextView.text = "Phrase: " + gameAnswerhash.toUpperCase()
        LetterstextView.text = "Guessed Letters: "+guessedLetters.toUpperCase()
        if(isguessPhrase){
            userguess.hint = "Guess the full phrase"
        }else{
            userguess.hint = "Guess a letter"
        }
    }
    private fun Alert(message:String){
        // first we create a variable to hold an AlertDialog builder
        val dialogBuilder = AlertDialog.Builder(this)
        // then we set up the input

        // here we set the message of our alert dialog
        dialogBuilder.setTitle(message)
            // positive button text and action
            .setPositiveButton("Try again", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
                messages.clear()
                guessedLetters=""

            })
            // negative button text and action
        // create dialog box
        val alert = dialogBuilder.create()
        // show alert dialog
        alert.show()
    }

}