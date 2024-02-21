package com.example.tictactoe.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tictactoe.models.Game

class GameViewModelFactory(val game: Game) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(p0: Class<T>): T = GameViewModel(game) as T
}