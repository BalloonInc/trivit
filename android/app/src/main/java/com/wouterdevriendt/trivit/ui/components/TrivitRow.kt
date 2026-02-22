package com.wouterdevriendt.trivit.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wouterdevriendt.trivit.R
import com.wouterdevriendt.trivit.data.model.Trivit
import kotlin.math.abs
import kotlin.math.roundToInt

private val DECREMENT_THRESHOLD = -60f // dp
private val DELETE_THRESHOLD = -200f // dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrivitRow(
    trivit: Trivit,
    backgroundColor: Color,
    darkBackgroundColor: Color,
    hideCounter: Boolean,
    onTap: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onDelete: () -> Unit,
    onRename: (String) -> Unit,
    onColorChange: () -> Unit,
    onReset: () -> Unit,
    onStatistics: () -> Unit,
    onHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }
    var editTextFieldValue by remember(trivit.name) {
        mutableStateOf(TextFieldValue(trivit.name, TextRange(0, trivit.name.length)))
    }
    var showContextMenu by remember { mutableStateOf(false) }
    var dragOffsetPx by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val density = LocalDensity.current

    val useChinese = trivit.name.startsWith("_")

    // Convert dp thresholds to px
    val decrementThresholdPx = with(density) { DECREMENT_THRESHOLD.dp.toPx() }
    val deleteThresholdPx = with(density) { DELETE_THRESHOLD.dp.toPx() }

    // Spring animation for snap-back
    val animatedOffsetPx by animateFloatAsState(
        targetValue = if (isDragging) dragOffsetPx else 0f,
        animationSpec = if (isDragging) {
            SpringSpec(stiffness = Spring.StiffnessHigh)
        } else {
            SpringSpec(
                dampingRatio = 0.7f,
                stiffness = Spring.StiffnessMediumLow
            )
        },
        label = "swipeOffset"
    )

    val isDeleteZone = animatedOffsetPx < deleteThresholdPx
    val isDecrementZone = animatedOffsetPx < decrementThresholdPx && !isDeleteZone

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
    ) {
        // ZStack: swipe background behind main content
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Swipe background (revealed behind content)
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        if (isDeleteZone) Color.Red
                        else backgroundColor.copy(alpha = 0.7f)
                    ),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (animatedOffsetPx < 0f) {
                    Icon(
                        imageVector = if (isDeleteZone) Icons.Default.Delete else Icons.Default.Remove,
                        contentDescription = null,
                        tint = Color.White.copy(
                            alpha = if (isDecrementZone || isDeleteZone) 1f else 0.4f
                        ),
                        modifier = Modifier.padding(end = 24.dp)
                    )
                }
            }

            // Main content (slides with drag)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(animatedOffsetPx.roundToInt(), 0) }
            ) {
                // Title bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragStart = {
                                    dragOffsetPx = 0f
                                    isDragging = true
                                },
                                onDragEnd = {
                                    when {
                                        dragOffsetPx < deleteThresholdPx -> onDelete()
                                        dragOffsetPx < decrementThresholdPx -> onDecrement()
                                    }
                                    isDragging = false
                                    dragOffsetPx = 0f
                                },
                                onDragCancel = {
                                    isDragging = false
                                    dragOffsetPx = 0f
                                },
                                onHorizontalDrag = { _, dragAmount ->
                                    // Only allow left swipe
                                    val newOffset = dragOffsetPx + dragAmount
                                    if (newOffset <= 0f) {
                                        dragOffsetPx = newOffset
                                    }
                                }
                            )
                        }
                        .combinedClickable(
                            onClick = {
                                if (!isEditing) onTap()
                            },
                            onLongClick = {
                                // Long press on title bar -> inline rename with select-all
                                editTextFieldValue = TextFieldValue(
                                    trivit.name,
                                    TextRange(0, trivit.name.length)
                                )
                                isEditing = true
                            }
                        )
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isEditing) {
                            BasicTextField(
                                value = editTextFieldValue,
                                onValueChange = { editTextFieldValue = it },
                                textStyle = TextStyle(
                                    color = Color.White.copy(alpha = 0.95f),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                singleLine = true,
                                cursorBrush = SolidColor(Color.White),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        val text = editTextFieldValue.text
                                        if (text.isNotBlank()) {
                                            onRename(text)
                                        }
                                        isEditing = false
                                        focusManager.clearFocus()
                                    }
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .focusRequester(focusRequester)
                            )
                            LaunchedEffect(Unit) {
                                focusRequester.requestFocus()
                            }
                        } else {
                            Text(
                                text = trivit.name,
                                color = Color.White.copy(alpha = 0.95f),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (!hideCounter || !trivit.isExpanded) {
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = trivit.count.toString(),
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.End
                            )
                        }
                    }

                    // Context menu
                    DropdownMenu(
                        expanded = showContextMenu,
                        onDismissRequest = { showContextMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.statistics)) },
                            onClick = {
                                showContextMenu = false
                                onStatistics()
                            },
                            leadingIcon = { Icon(Icons.Default.BarChart, contentDescription = null) }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.history)) },
                            onClick = {
                                showContextMenu = false
                                onHistory()
                            },
                            leadingIcon = { Icon(Icons.Default.History, contentDescription = null) }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.rename)) },
                            onClick = {
                                showContextMenu = false
                                editTextFieldValue = TextFieldValue(
                                    trivit.name,
                                    TextRange(0, trivit.name.length)
                                )
                                isEditing = true
                            },
                            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.change_color)) },
                            onClick = {
                                showContextMenu = false
                                onColorChange()
                            },
                            leadingIcon = { Icon(Icons.Default.ColorLens, contentDescription = null) }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.reset_count)) },
                            onClick = {
                                showContextMenu = false
                                onReset()
                            },
                            leadingIcon = { Icon(Icons.Default.Refresh, contentDescription = null) }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error) },
                            onClick = {
                                showContextMenu = false
                                onDelete()
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }
                }

                // Expandable tally marks area
                AnimatedVisibility(
                    visible = trivit.isExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column {
                        // Triangle indicator pointing down
                        val triangleColor = backgroundColor
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(9.dp)
                                .background(backgroundColor.copy(alpha = 0.55f))
                                .drawBehind {
                                    val triangleWidth = 18.dp.toPx()
                                    val triangleHeight = 9.dp.toPx()
                                    val leftOffset = 20.dp.toPx()
                                    val path = Path().apply {
                                        moveTo(leftOffset, 0f)
                                        lineTo(leftOffset + triangleWidth, 0f)
                                        lineTo(leftOffset + triangleWidth / 2f, triangleHeight)
                                        close()
                                    }
                                    drawPath(path, triangleColor)
                                }
                        )

                        // Tally marks area
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(backgroundColor.copy(alpha = 0.55f))
                                .combinedClickable(
                                    onClick = onIncrement
                                )
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            if (trivit.count > 0) {
                                TallyMarksView(
                                    count = trivit.count,
                                    color = Color.White.copy(alpha = 0.9f),
                                    useChinese = useChinese
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(36.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Empty space, min height matches iOS
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
