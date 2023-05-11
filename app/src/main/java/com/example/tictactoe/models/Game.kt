package com.example.tictactoe.models

import com.example.tictactoe.utils.*

class Game(private val configuration: GameConfiguration) {
    private var cells: Array<Array<CellState>>
    private var stateType: GameStateType
    private var winnerCheckingResult: WinnerCheckingResult?
    private val computer: AIPlayer

    init {
        winnerCheckingResult = null
        cells = Array(configuration.boxSize) {
            Array(configuration.boxSize) { CellState.Empty }
        }
        stateType = if (configuration.mode == GameMode.CrossComputer)
            GameStateType.ZeroStep
        else
            GameStateType.CrossStep
        computer = AIPlayer(this)
    }

    fun getGameConfiguration(): GameConfiguration =
        configuration

    fun getGameState(): GameState =
        GameState(cells, stateType, winnerCheckingResult)

    fun ZeroStep(iRow: Int, iCol: Int) {
        setZero(iRow, iCol)
        updateState()
        setComputedCell()
        updateState()
    }

    fun CrossStep(iRow: Int, iCol: Int) {
        setCross(iRow, iCol)
        updateState()
        setComputedCell()
        updateState()
    }

    fun CreateNewGame() {
        reset()
    }

    private fun updateState() {
        if (stateType == GameStateType.ZeroStep || stateType == GameStateType.CrossStep) {
            winnerCheckingResult = checkCrossWinner()
            if (winnerCheckingResult != null) {
                stateType = GameStateType.CrossWinner
                return
            }

            winnerCheckingResult = checkZeroWinner()
            if (winnerCheckingResult != null) {
                stateType = GameStateType.ZeroWinner
                return
            }

            if (!existsFreeCells()) {
                stateType = GameStateType.Nothing
                return
            }

            stateType = if(stateType == GameStateType.ZeroStep)
                GameStateType.CrossStep
            else
                GameStateType.ZeroStep
        }
    }

    private fun setComputedCell() {
        if (existsFreeCells()) {
            val cell = computer.getNextStep()
            if (configuration.mode == GameMode.CrossComputer)
                setCross(cell.first, cell.second)
            else
                setZero(cell.first, cell.second)
        }
    }

    private fun setZero(iRow: Int, iCol: Int) {
        if(stateType == GameStateType.ZeroStep && isCellFree(iRow, iCol))
            cells[iRow][iCol] = CellState.Zero
    }

    private fun setCross(iRow: Int, iCol: Int) {
        if(stateType == GameStateType.CrossStep && isCellFree(iRow, iCol))
            cells[iRow][iCol] = CellState.Cross
    }

    private fun reset() {
        winnerCheckingResult = null
        cells = Array(configuration.boxSize) {
            Array(configuration.boxSize) { CellState.Empty }
        }
        stateType = if (configuration.mode == GameMode.CrossComputer)
            GameStateType.ZeroStep
        else
            GameStateType.CrossStep
    }

    private fun existsFreeCells(): Boolean =
        cells.flatMap { it.asSequence() }.any { it == CellState.Empty }

    private fun isCellFree(iRow: Int, iCol: Int): Boolean =
        cells[iRow][iCol] == CellState.Empty

    private fun checkWinnerCombination(forCross: Boolean): WinnerCheckingResult? {
        fun getRowWinnerResult(playerType: CellState): WinnerCheckingResult? {
            val iWinnerRow = getFirstSimilarIRow<CellState>(cells, playerType)
            return if (iWinnerRow != null)
                WinnerCheckingResult(WinnerDirectionType.Row, iWinnerRow)
            else
                null
        }

        fun getColWinnerResult(playerType: CellState): WinnerCheckingResult? {
            val iWinnerCol = getFirstSimilarICol<CellState>(cells, playerType)
            return if (iWinnerCol != null)
                WinnerCheckingResult(WinnerDirectionType.Column, iWinnerCol)
            else
                null
        }

        fun getMainDiagWinnerResult(playerType: CellState): WinnerCheckingResult? {
            val iMainDiagWin = isSimilarMainDiag<CellState>(cells, playerType)
            return if (iMainDiagWin)
                WinnerCheckingResult(WinnerDirectionType.MainDiag)
            else
                null
        }

        fun getSideDiagWinnerResult(playerType: CellState): WinnerCheckingResult? {
            val iSideDiagWin = isSimilarSideDiag<CellState>(cells, playerType)
            return if (iSideDiagWin)
                WinnerCheckingResult(WinnerDirectionType.SideDiag)
            else
                null
        }

        val playerType = if (forCross) CellState.Cross else CellState.Zero

        return getRowWinnerResult(playerType) ?:
        getColWinnerResult(playerType) ?:
        getMainDiagWinnerResult(playerType) ?:
        getSideDiagWinnerResult(playerType)
    }

    private fun checkCrossWinner(): WinnerCheckingResult? =
        checkWinnerCombination(true)

    private fun checkZeroWinner(): WinnerCheckingResult? =
        checkWinnerCombination(false)
}