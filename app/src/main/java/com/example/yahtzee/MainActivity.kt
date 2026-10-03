package com.example.yahtzee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yahtzee.YahtzeeScreen
import com.example.yahtzee.YahtzeeViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: YahtzeeViewModel = viewModel()

            YahtzeeScreen(
                viewModel = viewModel
            )
        }
    }
}