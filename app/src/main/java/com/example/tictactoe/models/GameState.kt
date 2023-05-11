package com.example.tictactoe.models

data class GameState(
    val world: Array<Array<CellState>>,
    val stateType: GameStateType,
    val winnerCheckingResult: WinnerCheckingResult? = null
)
