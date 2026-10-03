package com.example.yahtzee

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.random.Random
//import android.os.Handler
//import android.os.Looper
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class YahtzeeViewModel : ViewModel() {
//    private val mainHandler = Handler(
//        Looper.getMainLooper()
//    )

    var categoryScores by mutableStateOf(
        emptyList<CategoryScore>()
    )
        private set

    var diceValues by mutableStateOf(
        listOf(1, 1, 1, 1, 1)
    )
        private set

    private fun rollDice(): Int{
        return Random.nextInt(1,7)
    }

//    fun rollOnce(){
////        diceValues = List(5){
////            rollDice()
////        }
//        repeat(10) {
//
//            diceValues = List(5) {
//                rollDice()
//            }
//
//            Thread.sleep(100)
//        }
//    }
//    fun rollWithoutCoroutine() {
//
//        Thread {
//
//            repeat(10) {
//
//                val newValues = List(5) {
//                    rollDice()
//                }
//
//                mainHandler.post {
//
//                    diceValues = newValues
//                }
//
//                Thread.sleep(100)
//            }
//
//        }.start()
//    }
    fun rollWithCoroutine() {

        viewModelScope.launch {

            repeat(10) {

//                diceValues = List(5) {
//                    rollDice()
//                }

                diceValues = listOf(
                    2,2,2,5,6
                )

                evaluateDice()

                delay(100.milliseconds)
            }
        }
    }
    private fun evaluateDice() {

        categoryScores =
            DiceRules
                .getAvailableCategories(diceValues)
                .map { category ->

                    CategoryScore(
                        category = category,
                        score = DiceRules.scoreFor(
                            category = category,
                            dice = diceValues
                        )
                    )
                }
    }
}