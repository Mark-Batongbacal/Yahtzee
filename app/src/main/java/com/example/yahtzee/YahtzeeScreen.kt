package com.example.yahtzee

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun YahtzeeScreen(viewModel: YahtzeeViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Yahtzee")

        DiceSection(viewModel)

        ControlsSection(viewModel)

        CategorySection(viewModel)

        ScoreSection(viewModel)
    }
}

@Composable
private fun DiceSection(viewModel: YahtzeeViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        viewModel.diceValues.forEachIndexed { index, value ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { viewModel.toggleHold(index) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Die(
                    value = value,
                    modifier = Modifier.size(48.dp)
                )

                Text(
                    if (viewModel.holdDice[index]) "HELD" else "Hold"
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ControlsSection(viewModel: YahtzeeViewModel) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = { viewModel.rollWithCoroutine() },
            enabled = viewModel.canRoll
        ) {
            Text("Roll (${viewModel.rollCount}/3)")
        }

        Button(
            onClick = { viewModel.startNewTurn() },
            enabled = viewModel.canRoll || viewModel.gameOver
        ) {
            Text("New Turn")
        }

        Button(
            onClick = { viewModel.restartGame() },
            enabled = !viewModel.isRolling
        ) {
            Text("Restart")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategorySection(viewModel: YahtzeeViewModel) {
    Text("Choose Category")

    if (viewModel.gameOver) {
        Text("GAME OVER!")
        Text("Final Score: ${viewModel.totalScore}")
    } else if (viewModel.rollCount > 0) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            viewModel.availableCategories.forEach { category ->
                Button(
                    onClick = { viewModel.chooseCategory(category) },
                    enabled = viewModel.canChooseCategory
                ) {
                    Text(
                        "$category: ${
                            DiceRules.scoreFor(
                                category,
                                viewModel.diceValues
                            )
                        }"
                    )
                }
            }
        }
    }
}

@Composable
private fun ScoreSection(viewModel: YahtzeeViewModel) {
    Text("Scorecard")

    viewModel.savedScores.forEach { result ->
        Text("${result.category}: ${result.score}")
    }

    Text("Total Score: ${viewModel.totalScore}")
}

@Composable
fun Die(value: Int, modifier: Modifier) {
    val diceImage = when (value) {
        1 -> R.drawable.die_1
        2 -> R.drawable.die_2
        3 -> R.drawable.die_3
        4 -> R.drawable.die_4
        5 -> R.drawable.die_5
        6 -> R.drawable.die_6
        else -> R.drawable.die_1
    }

    Image(
        painter = painterResource(id = diceImage),
        contentDescription = "Dice showing $value",
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}