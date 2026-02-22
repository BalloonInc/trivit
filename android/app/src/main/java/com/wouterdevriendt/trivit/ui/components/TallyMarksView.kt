package com.wouterdevriendt.trivit.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

private const val GROUPS_PER_ROW = 10
private const val MARKS_PER_GROUP = 5

@Composable
fun TallyMarksView(
    count: Int,
    modifier: Modifier = Modifier,
    color: Color = Color.White.copy(alpha = 0.9f),
    useChinese: Boolean = false
) {
    val fullGroups = count / MARKS_PER_GROUP
    val remainder = count % MARKS_PER_GROUP
    val totalGroups = fullGroups + if (remainder > 0) 1 else 0
    val rows = if (totalGroups == 0) 0 else ((totalGroups - 1) / GROUPS_PER_ROW) + 1
    val rowHeight = if (useChinese) 26.dp else 26.dp
    val totalHeight = (rows * rowHeight.value).coerceAtLeast(20f)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(totalHeight.dp)
    ) {
        if (useChinese) {
            drawChineseTallyMarks(count, color)
        } else {
            drawWesternTallyMarks(count, color)
        }
    }
}

private fun DrawScope.drawWesternTallyMarks(count: Int, color: Color) {
    // Match iOS: 2px wide, 16px tall, 3px spacing between lines in a group
    val strokeWidth = 2.dp.toPx()
    val lineHeight = 16.dp.toPx()
    val lineSpacing = 3.dp.toPx() + strokeWidth  // 3dp gap + line width for center-to-center
    val groupSpacing = 8.dp.toPx()  // gap between groups
    val rowHeight = 26.dp.toPx()
    val topPadding = 4.dp.toPx()
    val leftPadding = 4.dp.toPx()

    val fullGroups = count / MARKS_PER_GROUP
    val remainder = count % MARKS_PER_GROUP

    var groupIndex = 0

    for (g in 0 until fullGroups) {
        val row = groupIndex / GROUPS_PER_ROW
        val col = groupIndex % GROUPS_PER_ROW
        val groupWidth = 3 * lineSpacing  // 4 lines, 3 gaps
        val baseX = leftPadding + col * (groupWidth + groupSpacing)
        val baseY = topPadding + row * rowHeight

        // Draw 4 vertical lines
        for (i in 0 until 4) {
            val x = baseX + i * lineSpacing
            drawLine(
                color = color,
                start = Offset(x, baseY),
                end = Offset(x, baseY + lineHeight),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }

        // Draw diagonal line at ~30 degrees
        // iOS: rotationEffect(.degrees(30)) on a line centered on the group
        // The diagonal should cross through all 4 vertical lines
        val diagonalHeight = 22.dp.toPx()  // slightly taller than the verticals
        val centerX = baseX + 1.5f * lineSpacing
        val centerY = baseY + lineHeight / 2f
        val angle = Math.toRadians(30.0)
        val halfLen = diagonalHeight / 2f
        val dx = (halfLen * sin(angle)).toFloat()
        val dy = (halfLen * cos(angle)).toFloat()
        drawLine(
            color = color,
            start = Offset(centerX - dx, centerY + dy),
            end = Offset(centerX + dx, centerY - dy),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        groupIndex++
    }

    // Draw remaining marks
    if (remainder > 0) {
        val row = groupIndex / GROUPS_PER_ROW
        val col = groupIndex % GROUPS_PER_ROW
        val groupWidth = 3 * lineSpacing
        val baseX = leftPadding + col * (groupWidth + groupSpacing)
        val baseY = topPadding + row * rowHeight

        for (i in 0 until remainder) {
            val x = baseX + i * lineSpacing
            drawLine(
                color = color,
                start = Offset(x, baseY),
                end = Offset(x, baseY + lineHeight),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        }
    }
}

private fun DrawScope.drawChineseTallyMarks(count: Int, color: Color) {
    // Match iOS: 正 character, 18x18 frame per character
    val strokeWidth = 2.dp.toPx()
    val charSize = 18.dp.toPx()
    val groupSpacing = 6.dp.toPx()
    val rowHeight = 26.dp.toPx()
    val topPadding = 4.dp.toPx()
    val leftPadding = 4.dp.toPx()

    val fullGroups = count / MARKS_PER_GROUP
    val remainder = count % MARKS_PER_GROUP

    var groupIndex = 0

    // Draw full groups (complete 正 characters)
    for (g in 0 until fullGroups) {
        val row = groupIndex / GROUPS_PER_ROW
        val col = groupIndex % GROUPS_PER_ROW
        val baseX = leftPadding + col * (charSize + groupSpacing)
        val baseY = topPadding + row * rowHeight
        drawZhengCharacter(baseX, baseY, charSize, 5, color, strokeWidth)
        groupIndex++
    }

    // Draw partial group
    if (remainder > 0) {
        val row = groupIndex / GROUPS_PER_ROW
        val col = groupIndex % GROUPS_PER_ROW
        val baseX = leftPadding + col * (charSize + groupSpacing)
        val baseY = topPadding + row * rowHeight
        drawZhengCharacter(baseX, baseY, charSize, remainder, color, strokeWidth)
    }
}

private fun DrawScope.drawZhengCharacter(
    x: Float, y: Float, size: Float, strokes: Int,
    color: Color, strokeWidth: Float
) {
    // 正 character matching iOS layout
    // iOS dimensions: 18x18 frame
    // Stroke 1: top horizontal at y=-7 (relative to center), width=14
    // Stroke 2: left vertical at x=-5, height=16
    // Stroke 3: middle horizontal at y=0, width=10
    // Stroke 4: right vertical at x=5, height=8, offset y=4
    // Stroke 5: bottom horizontal at y=7, width=14
    val cx = x + size / 2f
    val cy = y + size / 2f

    // Stroke 1: top horizontal
    if (strokes >= 1) {
        drawLine(
            color = color,
            start = Offset(cx - 7.dp.toPx(), cy - 7.dp.toPx()),
            end = Offset(cx + 7.dp.toPx(), cy - 7.dp.toPx()),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
    // Stroke 2: left vertical
    if (strokes >= 2) {
        drawLine(
            color = color,
            start = Offset(cx - 5.dp.toPx(), cy - 8.dp.toPx()),
            end = Offset(cx - 5.dp.toPx(), cy + 8.dp.toPx()),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
    // Stroke 3: middle horizontal
    if (strokes >= 3) {
        drawLine(
            color = color,
            start = Offset(cx - 5.dp.toPx(), cy),
            end = Offset(cx + 5.dp.toPx(), cy),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
    // Stroke 4: right vertical (shorter, offset down)
    if (strokes >= 4) {
        drawLine(
            color = color,
            start = Offset(cx + 5.dp.toPx(), cy),
            end = Offset(cx + 5.dp.toPx(), cy + 8.dp.toPx()),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
    // Stroke 5: bottom horizontal
    if (strokes >= 5) {
        drawLine(
            color = color,
            start = Offset(cx - 7.dp.toPx(), cy + 7.dp.toPx()),
            end = Offset(cx + 7.dp.toPx(), cy + 7.dp.toPx()),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}
