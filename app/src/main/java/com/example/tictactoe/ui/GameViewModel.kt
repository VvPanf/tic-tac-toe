package com.example.tictactoe.ui

import androidx.lifecycle.ViewModel
import com.example.tictactoe.models.Game
import com.example.tictactoe.models.GameConfiguration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel(game: Game): ViewModel() {
    private val _game: Game = game
    private val _uiState = MutableStateFlow(GameUiState(game))

    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun zeroStep(iRow: Int, iCol: Int) {
        _game.ZeroStep(iRow, iCol)
        _uiState.update { GameUiState(_game) }
    }

    fun crossStep(iRow: Int, iCol: Int) {
        _game.CrossStep(iRow, iCol)
        _uiState.update { GameUiState(_game) }
    }

    fun startNewGame() {
        _game.CreateNewGame()
        _uiState.update { GameUiState(_game) }
    }

    fun getGameConfiguration(): GameConfiguration =
        _game.getGameConfiguration()

    fun changeTheme() =
        _isDarkTheme.update { !it }

}