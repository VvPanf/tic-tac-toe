package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.example.tictactoe.models.*
import com.example.tictactoe.ui.GameViewModel
import com.example.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    private val gameViewModel = GameViewModel(Game(GameConfiguration(GameMode.CrossComputer, 3)))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkTheme by gameViewModel.isDarkTheme.collectAsState()
            TicTacToeTheme(darkTheme = isDarkTheme) {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Greeting()
                }
            }
        }
    }

    @Composable
    fun Greeting() {
        Scaffold (
            topBar = {
                TopBar()
            },
            content = {
                Content(it)
            },
            bottomBar = {
                BottomBar()
            }
        )
    }

    @Composable
    fun TopBar() {
        var expanded by remember { mutableStateOf(false) }

        TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
            Text(text = stringResource(R.string.app_header))
            Box(
                modifier = Modifier.fillMaxWidth()
                    .wrapContentSize(Alignment.TopEnd)
            ) {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(onClick = { gameViewModel.startNewGame() }) {
                        Text(stringResource(R.string.menu_item_new_game))
                    }
                    Divider()
                    DropdownMenuItem(onClick = { gameViewModel.changeTheme() }) {
                        Text(stringResource(R.string.menu_item_change_theme))
                    }
                    Divider()
                    DropdownMenuItem(onClick = { finish() }) {
                        Text(stringResource(R.string.menu_item_exit))
                    }
                }
            }
        }
    }

    @Composable
    fun Content(paddingValues: PaddingValues) {
        val uiState by gameViewModel.uiState.collectAsState()
        val boxSize = gameViewModel.getGameConfiguration().boxSize
        Column (Modifier.fillMaxWidth()) {
            for (i in 0 until boxSize) {
                Row {
                    for (j in 0 until boxSize) {
                        IconButton(
                            modifier = Modifier
                                .weight(.3f, true)
                                .background(Color.LightGray)
                                .border(Dp(.5f), Color.Black)
                                .padding(paddingValues),
                            onClick = { if (uiState.cells[i][j].cellType == CellState.Empty) gameViewModel.zeroStep(i, j) }
                        ) {
                            when (uiState.cells[i][j].cellType) {
                                CellState.Cross -> Icon(ImageVector.vectorResource(R.drawable.cross_icon), contentDescription = "Cross", tint = MaterialTheme.colors.secondaryVariant)
                                CellState.Zero -> Icon(ImageVector.vectorResource(R.drawable.zero_icon), contentDescription = "Zero", tint = MaterialTheme.colors.secondary)
                                CellState.Empty -> Icon(ImageVector.vectorResource(R.drawable.empty_icon), contentDescription = "Empty")
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun BottomBar() {
        val uiState by gameViewModel.uiState.collectAsState()
        when (uiState.stateType) {
            GameStateType.CrossWinner -> Snackbar {
                Text(text = stringResource(R.string.game_info_cross_win))
            }
            GameStateType.ZeroWinner -> Snackbar {
                Text(text = stringResource(R.string.game_info_zero_win))
            }
            else -> {}
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        TicTacToeTheme {
            Greeting()
        }
    }
}

