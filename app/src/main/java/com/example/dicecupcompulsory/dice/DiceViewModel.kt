package com.example.dicecupcompulsory.dice

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.dicecupcompulsory.R
import kotlinx.android.synthetic.main.roll.view.*


private const val TAG= "DiceViewModel"
class DiceViewModel : ViewModel() {

    var diceRollListFragment: DiceRollListFragment
    val dice = mutableListOf<Dice>()

    //region vals and vars
    var diceListFragment: DiceListFragment
    var currentDiceAmount = 2

    var diceHistoryManager : DiceHistoryManager


    // mapping from 1..6 to drawables, the first index is unused
    val diceImages = intArrayOf(
        0, R.drawable.dice1,
        R.drawable.dice2,
        R.drawable.dice3,
        R.drawable.dice4,
        R.drawable.dice5,
        R.drawable.dice6
    )

    //endregion

    init {
        Log.d(TAG, "ViewModel instance created")
        diceHistoryManager = DiceHistoryManager();
        diceListFragment = DiceListFragment()
        diceRollListFragment = DiceRollListFragment()

    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel about to be destroyed")
    }

    fun roll(){
        diceListFragment.rollDice(diceHistoryManager)
    }

    fun addDiceRollToView(view: View, rollLog: DiceRollLog) {

        view.txtRollText.text = rollLog.diceAmountString
        view.txtRollText.setTypeface(null, Typeface.BOLD)
        view.txtRollText.setTextColor(Color.WHITE)

        val allDices = listOf(view.dice1, view.dice2, view.dice3, view.dice4, view.dice5, view.dice6, view.dice7, view.dice8, view.dice9)

        //For each dice, if the dice has an index lower than the total amount of dices in the current diceRollLog, the dice will be shown with an image;
        for((i, dice) in allDices.withIndex()){
            if(i<rollLog.diceAmount){
                dice.visibility = View.VISIBLE
                allDices[i].setImageResource(diceImages[rollLog.dices[i]])
            }
            else{
                dice.visibility = View.GONE
            }
        }

    }

    fun updateFragmentDiceFromHistory() {
        if(diceHistoryManager.historyList.isNotEmpty()){
            diceListFragment.setDiceFromRoll(diceHistoryManager.getLastRoll())
        }
    }

    fun updateDice() {
        if(dice.size < currentDiceAmount)
            while(dice.size < currentDiceAmount){
                dice.add(Dice())
            }

        else if(dice.size > currentDiceAmount){
            var diff = dice.size-currentDiceAmount
            while (diff>0){
                dice.removeAt(dice.size - diff)
                diff --
            }
        }

    }

    fun rollDice(diceHistoryManager: DiceHistoryManager) {

        val diceRolls = ArrayList<Int>()
        for(die in dice){
            die.roll()
            diceRolls.add(die.currentEyes)
        }

        diceHistoryManager.addToHistory(DiceRollLog(diceRolls))
    }

    fun updateDiceFromHistory(lastRoll: DiceRollLog) {

        currentDiceAmount = lastRoll.diceAmount
        updateDice()
        for((i, die) in lastRoll.dices.withIndex()){
            dice[i].currentEyes = die
        }
    }


}