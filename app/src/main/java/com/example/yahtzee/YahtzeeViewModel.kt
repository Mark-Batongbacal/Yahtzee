
package com.example.yahtzee

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class YahtzeeViewModel : ViewModel() {

    var categoryScores by mutableStateOf(emptyList<CategoryScore>())
        private set

    var diceValues by mutableStateOf(listOf(1, 1, 1, 1, 1))
        private set

    var holdDice by mutableStateOf(listOf(false, false, false, false, false))
        private set

    var rollCount by mutableStateOf(0)
        private set

    var savedScores by mutableStateOf(emptyList<CategoryScore>())
        private set

    var isRolling by mutableStateOf(false)
        private set

    val availableCategories: List<YahtzeeCategory>
        get() = YahtzeeCategory.values().filter { category ->
            savedScores.none { it.category == category }
        }

    val totalScore: Int
        get() = savedScores.sumOf { it.score }

    val gameOver: Boolean
        get() = availableCategories.isEmpty()

    val canRoll: Boolean
        get() = !gameOver && rollCount < 3 && !isRolling

    val canChooseCategory: Boolean
        get() = !gameOver && rollCount > 0 && !isRolling

    private fun rollDice(): Int = Random.nextInt(1, 7)

    fun toggleHold(index: Int) {
        if (rollCount == 0 || rollCount >= 3) return
        if (isRolling || index !in holdDice.indices) return

        holdDice = holdDice.toMutableList().also {
            it[index] = !it[index]
        }
    }

    fun rollWithCoroutine() {
        if (!canRoll) return

        rollCount++
        isRolling = true

        viewModelScope.launch {
            try {
                repeat(10) {
                    diceValues = diceValues.mapIndexed { index, value ->
                        if (holdDice[index]) value else rollDice()
                    }
                    delay(100.milliseconds)
                }
                evaluateDice()
            } finally {
                isRolling = false
            }
        }
    }

    private fun evaluateDice() {
        categoryScores = availableCategories.map { category ->
            CategoryScore(
                category = category,
                score = DiceRules.scoreFor(category, diceValues)
            )
        }
    }

    fun chooseCategory(category: YahtzeeCategory) {
        if (!canChooseCategory || category !in availableCategories) return

        savedScores = savedScores + CategoryScore(
            category = category,
            score = DiceRules.scoreFor(category, diceValues)
        )

        if (!gameOver) {
            startNewTurn()
        } else {
            categoryScores = emptyList()
        }
    }

    fun startNewTurn() {
        isRolling = false
        rollCount = 0
        holdDice = listOf(false, false, false, false, false)
        diceValues = listOf(1, 1, 1, 1, 1)
        categoryScores = emptyList()
    }

    fun restartGame() {
        savedScores = emptyList()
        startNewTurn()
    }
}