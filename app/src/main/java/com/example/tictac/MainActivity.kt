package com.example.tictac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val button :Array<Array<Button?>> = Array<Array<Button?>>(size = 3){ arrayOfNulls<Button>(size = 3)}
    private var player1Turn = true
    private var roundCount = 0
    private var player1Points = 0
    private var player2Points = 0
    private lateinit var textviewP1:TextView
    private lateinit var textviewP2:TextView
    private var playerFirst = ""
    private var playerSecond = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textviewP1 = findViewById(R.id.text_viewP1)
        textviewP2 = findViewById(R.id.text_viewP2)
        getPlayerName()
        for (i in 0..2){
            for (j in 0..2) {
                val buttonId = "button_$i$j"
                val resId = resources.getIdentifier(
                    buttonId,
                    "id", packageName)
                button[i][j] = findViewById(resId)
                button[i][j]!!.setOnClickListener(this)
            }
        }
        val resetBtn = findViewById<Button>(R.id.resetBtn)
        resetBtn.setOnClickListener{ resetGame() }
    }

    private fun resetGame() {
        player1Points = 0
        player2Points = 0
        upDatePointsText()
        resetBord()
    }

    private fun getPlayerName(){
        val infalter = LayoutInflater.from(this@MainActivity)
        val plView = infalter.inflate(R.layout.player_info,null)
        val player1Name= plView.findViewById<EditText>(R.id.firstPl1)
        val player2Name= plView.findViewById<EditText>(R.id.firstPl2)
        val plDialog = AlertDialog.Builder(this@MainActivity)
        plDialog.setView(plView)
        plDialog.setPositiveButton("Add"){
            dialog,_->
            if ( player1Name.text.toString().isEmpty()
                && player2Name.text.toString().isEmpty()
            ){
                playerFirst = "Player1"
                playerSecond = "Player2"
                textviewP1.text="$playerFirst : 0"
                textviewP2.text="$playerSecond: 0"
            }
            else{
                playerFirst = player1Name.text.toString()
                playerSecond = player2Name.text.toString()
                textviewP1.text="$playerFirst : 0"
                textviewP2.text="$playerSecond: 0"
            }
        }
        plDialog.setNeutralButton("Cancel"){
            dialog,_->
            playerFirst = "Player1"
            playerSecond = "Player2"
            textviewP1.text="$playerFirst : 0"
            textviewP2.text="$playerSecond: 0"
        }
        plDialog.create()
        plDialog.show()
    }

    override fun onClick(v: View?) {
        if (!(v as Button).text.toString().equals("")){
            return
        }
        if(player1Turn){
            (v as Button).text = "X"
            v.setTextColor(resources.getColor(R.color.red))
        }
        else{
            (v as Button).text = "0"
            v.setTextColor(resources.getColor(R.color.green))
        }
        roundCount++
        if (checkForWin()){
            if(player1Turn){
                player1Win()
            }
            else{
                player2Win()
            }
        }
        else if (roundCount == 9){
            draw()
        }
        else{
            player1Turn = !player1Turn
        }
    }

    private fun draw() {
        AlertDialog.Builder(this)
            .setTitle("Draw !!")
            .setMessage("The game is drawn, Please Play Again...!!!")
            .setPositiveButton("ok"){dialog,_->
                resetBord()
            }
            .setNeutralButton("Cancel"){dialog,_->resetBord()}
            .create()
            .show()
    }

    private fun player2Win() {
        player2Points++
        AlertDialog.Builder(this@MainActivity)
            .setTitle("Congratulation !!")
            .setIcon(R.drawable.congo)
            .setMessage("Congratulation $playerSecond, you are win ... !!!")
            .create()
            .show()
        upDatePointsText()
        resetBord()
    }

    private fun player1Win() {
        player1Points++
        AlertDialog.Builder(this@MainActivity)
            .setTitle("Congratulation !!")
            .setIcon(R.drawable.congo)
            .setMessage("Congratulation $playerFirst, you are win ... !!!")
            .create()
            .show()
        upDatePointsText()
        resetBord()
    }

    private fun resetBord() {
        for (i in 0..2){
            for (j in 0..2){
                button[i][j]!!.text = ""
            }
        }
        roundCount = 0
        player1Turn=false
    }

    private fun upDatePointsText() {
        textviewP1.text = "$playerFirst : $player1Points"
        textviewP1.text = "$playerSecond : $player2Points"
    }

    private fun checkForWin() : Boolean{
        val field = Array(3){ arrayOfNulls<String>(3)}
        for (i in 0..2){
            for (j in 0..2){
                field[i][j]=button[i][j]!!.text.toString()
            }
        }
        for (i in 0..2){
            if (field[i][0]==field[i][1]
                && field[i][0]== field[i][2]
                && field[i][0]!=""
            ){
                return true
            }
        }
        for (i in 0..2) {
            if (field[0][i] == field[1][i]
                && field[0][i] == field[2][i]
                && field[0][i] !== ""
            ) {
                return true
            }
        }
        if (field[0][0] == field[1][1]
            && field[0][0] == field[2][2]
            && field[0][0] !=""
        ){
            return true
        }
        return if (field[0][2] == field[1][1]
            && field[0][2] == field[2][0]
            && field[0][2] !=""
        ){
            true
        }else false


    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt("roundCount",roundCount)
        outState.putInt("player1Points",player1Points)
        outState.putInt("player2Points",player2Points)
        outState.putBoolean("player1Turn",player1Turn)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        roundCount = savedInstanceState.getInt("roundCount")
        player1Points = savedInstanceState.getInt("player1Points")
        player2Points = savedInstanceState.getInt("player2Points")
        player1Turn = savedInstanceState.getBoolean("player1Turn")
    }
}