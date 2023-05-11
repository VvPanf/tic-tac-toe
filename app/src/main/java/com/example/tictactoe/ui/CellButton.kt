package com.example.tictactoe.ui

import com.example.tictactoe.models.CellState

data class CellButton(
    val cellType: CellState,
    val enabled: Boolean,
    val inWinLine: Boolean
    ) {
    constructor():
        this(cellType = CellState.Empty, enabled = true, inWinLine = false)
}