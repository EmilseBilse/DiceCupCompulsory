package com.example.dicecupcompulsory.dice

import java.io.Serializable

class DiceHistoryManager : Serializable {
    var historyList: MutableList<DiceRollLog> = ArrayList()

    fun addToHistory(history: DiceRollLog) {
        historyList.add(history)
    }

    fun getLastRoll(): DiceRollLog {
        return historyList[historyList.size-1]
    }


}