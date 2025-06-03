package com.example.myapplication

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var currentDiceAmount: Int = 0
    var diceTotal: Int = 0
    val diceFaces  = mutableListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val rollButton = findViewById<Button>(R.id.rollButton)
        val rollResultText = findViewById<TextView>(R.id.rollResultText)
        val diceAmountBar = findViewById<SeekBar>(R.id.diceAmountBar)
        val diceValueBar = findViewById<SeekBar>(R.id.diceValueBar)
        val diceParent = findViewById<ViewGroup>(R.id.diceBox)
        currentDiceAmount = diceAmountBar.progress

        for (i in 0 until diceParent.childCount){
            val child = diceParent.getChildAt(i)
            if (child is TextView){
                diceFaces.add(child)
            }
        }

        rollButton.setOnClickListener{
            diceTotal = rollDice(diceAmountBar.progress, diceValueBar.progress)
            rollResultText.text = diceTotal.toString()
        }

        diceAmountBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                while (progress < currentDiceAmount){
                    val removedValue = blankADie()
                    diceTotal -= removedValue
                    rollResultText.text = diceTotal.toString()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun rollDice(diceAmount: Int, maxValue: Int) : Int {
        var total = 0
        for (i in 0 until diceFaces.count()){
            var rand = 0
            if (maxValue > 0 && i < diceAmount) {
                rand = Random.nextInt(maxValue) + 1
            }
            diceFaces[i].text = rand.toString()
            total += rand
        }
        currentDiceAmount = diceAmount
        return total
    }

    private fun blankADie() : Int {
        val removedValue = diceFaces[currentDiceAmount - 1].text.toString().toInt()
        diceFaces[currentDiceAmount - 1].text = "0"
        currentDiceAmount -= 1
        return removedValue
    }
}