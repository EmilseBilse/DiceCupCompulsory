package com.example.dicecupcompulsory.dice

import com.example.dicecupcompulsory.Utils


data class Dice(var currentEyes: Int = 1) {

    val possibleRolls = intArrayOf(1,2,3,4,5,6)

    fun roll(){
        currentEyes = possibleRolls[Utils().getRandomInt(0, possibleRolls.size-1)]
    }


}