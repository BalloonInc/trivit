package com.wouterdevriendt.trivit.ui.screens.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wouterdevriendt.trivit.R
import com.wouterdevriendt.trivit.ui.components.ColorPickerDialog
import com.wouterdevriendt.trivit.ui.components.TrivitRow
import com.wouterdevriendt.trivit.ui.theme.TrivitColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrivitListScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToStatistics: (Long) -> Unit,
    onNavigateToHistory: (Long) -> Unit,
    viewModel: TrivitListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var colorPickerTrivitId by remember { mutableStateOf<Long?>(null) }
    var colorPickerCurrentIndex by remember { mutableStateOf(0) }
    val listState = rememberLazyListState()

    val deletedFormatString = stringResource(R.string.deleted_format)
    val undoString = stringResource(R.string.undo)

    // Handle undo snackbar
    LaunchedEffect(Unit) {
        viewModel.undoEvent.collect { deletedTrivit ->
            val message = deletedFormatString.replace("%1\$s", deletedTrivit.name)
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = undoString,
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete(deletedTrivit.id)
            }
        }
    }

    // Handle easter egg messages
    LaunchedEffect(uiState.easterEggMessage) {
        uiState.easterEggMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearEasterEgg()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings))
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.addTrivit() }) {
                        Icon(Icons.Default.Add, contentDescription = stringResource(R.string.new_trivit))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.trivits.isEmpty()) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_trivits_yet),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.tap_plus_to_create),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(4.dp)) }

                    items(
                        items = uiState.trivits,
                        key = { it.id }
                    ) { trivit ->
                        val paletteIndex = uiState.preferences.colorSchemeIndex
                        val bgColor = TrivitColors.getColor(paletteIndex, trivit.colorIndex)
                        val darkColor = TrivitColors.getDarkColor(paletteIndex, trivit.colorIndex)

                        TrivitRow(
                            trivit = trivit,
                            backgroundColor = bgColor,
                            darkBackgroundColor = darkColor,
                            hideCounter = uiState.preferences.hideCounterWhenExpanded,
                            onTap = { viewModel.toggleExpanded(trivit.id) },
                            onIncrement = { viewModel.increment(trivit.id) },
                            onDecrement = { viewModel.decrement(trivit.id) },
                            onDelete = { viewModel.softDelete(trivit.id) },
                            onRename = { name -> viewModel.rename(trivit.id, name) },
                            onColorChange = {
                                colorPickerTrivitId = trivit.id
                                colorPickerCurrentIndex = trivit.colorIndex
                            },
                            onReset = { viewModel.resetCount(trivit.id) },
                            onStatistics = { onNavigateToStatistics(trivit.id) },
                            onHistory = { onNavigateToHistory(trivit.id) },
                            modifier = Modifier.animateItem()
                        )
                    }

                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }

            // Tutorial overlay
            AnimatedVisibility(
                visible = uiState.showTutorial,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f))
                        .clickable { viewModel.dismissTutorial() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.welcome_to_trivit),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.tutorial_add) + "\n\n" +
                                    stringResource(R.string.tutorial_reorder) + "\n\n" +
                                    stringResource(R.string.tutorial_options),
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = stringResource(R.string.got_it),
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            // Color picker dialog
            colorPickerTrivitId?.let { trivitId ->
                val paletteIndex = uiState.preferences.colorSchemeIndex
                val palette = TrivitColors.getPalette(paletteIndex)
                ColorPickerDialog(
                    colors = palette.colors,
                    selectedIndex = colorPickerCurrentIndex,
                    onColorSelected = { index ->
                        viewModel.updateColor(trivitId, index)
                        colorPickerCurrentIndex = index
                    },
                    onDismiss = { colorPickerTrivitId = null }
                )
            }
        }
    }
}
