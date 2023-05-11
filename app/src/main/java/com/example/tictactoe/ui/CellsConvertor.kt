package com.example.tictactoe.ui

import com.example.tictactoe.models.CellState
import com.example.tictactoe.models.Game
import com.example.tictactoe.models.WinnerDirectionType

class CellsConvertor(private val game: Game) {

    fun convert(): Array<Array<CellButton>> {
        val rowLen = game.getGameConfiguration().boxSize
        val colLen = game.getGameConfiguration().boxSize
        val cells = game.getGameState().world

        val cellButton: ArrayList<Array<CellButton>> = ArrayList()
        var row: ArrayList<CellButton>

        for(iRow in 0 until rowLen) {
            row = ArrayList()
            for(iCol in 0 until colLen) {
                row.add(
                    CellButton(
                        cells[iRow][iCol],
                        cells[iRow][iCol] == CellState.Empty,
                        isWinLine(iRow, iCol)
                    )
                )
            }
            cellButton.add(row.toTypedArray())
        }

        return cellButton.toTypedArray()
    }

    private fun isWinLine(iRow: Int, iCol: Int): Boolean {
        val len = game.getGameConfiguration().boxSize
        val winState = game.getGameState().winnerCheckingResult
        if (winState != null) {
            return when {
                winState.indexType == WinnerDirectionType.Row && iRow == winState.index -> true
                winState.indexType == WinnerDirectionType.Column && iCol == winState.index -> true
                winState.indexType == WinnerDirectionType.MainDiag && iRow == iCol -> true
                winState.indexType == WinnerDirectionType.SideDiag && iRow + iCol == len - 1 -> true
                else -> false
            }
        }
        return false
    }
}