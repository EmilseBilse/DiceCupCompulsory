package com.example.dicecupcompulsory.dice

import androidx.lifecycle.ViewModel
import com.example.dicecupcompulsory.R

class DiceStoryViewModel:ViewModel() {

    var diceRollListFragment: DiceRollListFragment = DiceRollListFragment()
    var diceHistoryManager: DiceHistoryManager = DiceHistoryManager()

    val diceImages = intArrayOf(
        0, R.drawable.dice1,
        R.drawable.dice2,
        R.drawable.dice3,
        R.drawable.dice4,
        R.drawable.dice5,
        R.drawable.dice6
    )


}