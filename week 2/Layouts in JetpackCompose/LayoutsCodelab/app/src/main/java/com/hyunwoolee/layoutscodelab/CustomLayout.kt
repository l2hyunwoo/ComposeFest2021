package com.hyunwoolee.layoutscodelab

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Dp
import kotlin.math.max

// Using Compose's layout modifier: manually control how to measure and position an element
// measureable: child to be measured and placed
// constraints: minimum and maximum for the width and height of the child

fun Modifier.firstBaselineToTop(
    firstBaseLineToTop: Dp
) = this.then(
    layout { measurable, constraints ->
        // Measures the layout with constraints, returning a Placeable layout that has its new size.
        val placeable: Placeable = measurable.measure(constraints)

        // placeable의 FirstBaseLine이 있는 지 check
        // Returns the position of an alignment line, or AlignmentLine.Unspecified if the line is not provided.
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        val placeableY = firstBaseLineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            placeable.placeRelative(0, placeableY)
        }
    }
)

// Layout composable to manually control how to measure and position the layout's children
@Composable
fun CustomLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each child
            measurable.measure(constraints)
        }

        // Track the y co-ord we have placed children up to
        var yPosition = 0

        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = 0, y = yPosition)

                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }
    }
}

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Keep track of the width of each row
        val rowWidths = IntArray(rows) { 0 }

        // Keep track of the max height of each row
        val rowHeights = IntArray(rows) { 0 }
        val placeables = measurables.mapIndexed { index, measurable ->
            val placeable = measurable.measure(constraints)

            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = max(rowHeights[row], placeable.height)
            placeable
        }

        // 무조건 Constraint의 Size 이내에서 그리도록 한다
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth..constraints.maxWidth)
            ?: constraints.minWidth
        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight..constraints.maxHeight)

        val rowY = IntArray(rows) { 0 }
        for (index in 1 until rows) {
            rowY[index] = rowY[index - 1] + rowHeights[index - 1]
        }

        layout(width, height) {
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}
