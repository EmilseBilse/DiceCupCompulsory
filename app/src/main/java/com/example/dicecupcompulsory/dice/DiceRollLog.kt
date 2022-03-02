package com.example.dicecupcompulsory.dice

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class DiceRollLog(val dices: ArrayList<Int>) : Serializable {

    val date: Date = Calendar.getInstance().time;
    var format: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
    var formatted = format.format(date)

    val diceAmount: Int
        get() = dices.size

    val diceAmountString: String
        get() = "Amount of rolled dice: $diceAmount"

    val diceTimeString: String
        get() = "Time of Roll: $formatted"

    private val rollInstance: String
        get() {
            val str = StringBuilder("");
            for (i in dices.indices) {
                str.append(dices[i])
                if (i < dices.size - 1) {
                    str.append(" - ")
                }
            }
            return str.toString()
        }

    override fun toString(): String {
        return rollInstance
    }
}