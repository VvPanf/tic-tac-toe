package com.example.tictactoe.ui

import com.example.tictactoe.models.Game
import com.example.tictactoe.models.GameStateType

data class GameUiState(
    val cells: Array<Array<CellButton>>,
    val stateType: GameStateType
) {
    constructor(game: Game):
        this(cells = CellsConvertor(game).convert(),
            stateType = game.getGameState().stateType)
}
