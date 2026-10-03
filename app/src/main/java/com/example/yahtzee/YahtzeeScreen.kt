package com.example.yahtzee

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
@Composable
fun YahtzeeScreen(
    viewModel: YahtzeeViewModel
) {

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Text(
            text = "Yahtzee"
        )

        Row {

            viewModel.diceValues.forEach { value ->

                Die(
                    value = value
                )
            }
        }
        Button(
            onClick = {
                viewModel.rollWithCoroutine()
            }
        ) {
            Text("Roll")
        }
        viewModel.categoryScores.forEach { result ->

            Text(
                text = "${result.category}: ${result.score}"
            )
        }
    }
}

@Composable
fun Die(value: Int) {

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
        painter = painterResource(
            id = diceImage
        ),
        contentDescription = "Dice showing $value"
    )
}