package com.example.tictactoe.models

import com.example.tictactoe.utils.*

internal class AIPlayer(private val game: Game) {
    private var nextPos: Pair<Int, Int> = Pair(0, 0)

    fun getNextStep(): Pair<Int, Int> {
        val currentPlayer = if (game.getGameState().stateType == GameStateType.CrossStep)
            CellState.Cross
        else
            CellState.Zero
        minmax(game.getGameState().world.clone(), currentPlayer)
        return nextPos
    }

    private fun minmax(cells: Array<Array<CellState>>, currentPlayer: CellState): Int {
        if (gameCompleted(cells))
            return getScores(cells)
        val calcResult: Triple<Int, Int, Int>
        val scoresToPos: ArrayList<Triple<Int, Int, Int>> = arrayListOf()

        for (iRow in 0 until game.getGameConfiguration().boxSize) {
            for (iCol in 0 until game.getGameConfiguration().boxSize) {
                if (cells[iRow][iCol] == CellState.Empty) {
                    val newCells = cells.map { it.clone() }.toTypedArray()
                    newCells[iRow][iCol] = currentPlayer
                    scoresToPos.add(Triple(iRow, iCol, minmax(newCells, getNextPlayer(currentPlayer))))
                }
            }
        }

        calcResult = if (currentPlayer == getPlayerSymbol())
            scoresToPos.minBy { it.third }
        else
            scoresToPos.minBy { it.third }

        nextPos = Pair(calcResult.first, calcResult.second)
        return calcResult.third
    }

    private fun getScores(cells: Array<Array<CellState>>): Int =
        when {
            isWin(cells, getComputedSymbol()) -> 10
            isWin(cells, getPlayerSymbol()) -> -10
            else -> 0
        }

    private fun gameCompleted(cells: Array<Array<CellState>>): Boolean =
        allBusyCells(cells) || isWin(cells, getPlayerSymbol()) || isWin(cells, getComputedSymbol())

    private fun allBusyCells(cells: Array<Array<CellState>>): Boolean =
        cells.flatMap { it.asSequence() }.none { it == CellState.Empty }

    private fun isWin(cells: Array<Array<CellState>>, cellType: CellState): Boolean =
        getFirstSimilarIRow<CellState>(cells, cellType) != null ||
        getFirstSimilarICol<CellState>(cells, cellType) != null ||
        isSimilarMainDiag<CellState>(cells, cellType) ||
        isSimilarSideDiag<CellState>(cells, cellType)

    private fun getComputedSymbol(): CellState =
        if (game.getGameConfiguration().mode == GameMode.CrossComputer)
            CellState.Cross
        else
            CellState.Zero

    private fun getPlayerSymbol(): CellState =
        if (game.getGameConfiguration().mode == GameMode.CrossComputer)
            CellState.Zero
        else
            CellState.Cross

    private fun getNextPlayer(currentPlayer: CellState): CellState =
        if (currentPlayer == CellState.Zero)
            CellState.Cross
        else
            CellState.Zero
}