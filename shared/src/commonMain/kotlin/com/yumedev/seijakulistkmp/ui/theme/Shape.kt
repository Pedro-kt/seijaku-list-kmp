package com.yumedev.seijakulistkmp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

data class ExpressiveShapes(
    val small: Shape,
    val medium: Shape,
    val large: Shape,
    val extraLarge: Shape,
    val cardAsymmetric: Shape,
    val cardSymmetric: Shape,
    val buttonPrimary: Shape,
    val buttonSecondary: Shape,
    val chip: Shape,
    val badge: Shape,
    val searchBar: Shape,
    val bottomSheet: Shape,
)

val expressiveShapes = ExpressiveShapes(
    small = RoundedCornerShape(8.dp),

    medium = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 4.dp,
        bottomStart = 4.dp,
        bottomEnd = 16.dp
    ),

    large = RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 8.dp,
        bottomStart = 8.dp,
        bottomEnd = 24.dp
    ),

    extraLarge = RoundedCornerShape(
        topStart = 32.dp,
        topEnd = 12.dp,
        bottomStart = 12.dp,
        bottomEnd = 32.dp
    ),

    cardAsymmetric = RoundedCornerShape(
        topStart = 18.dp,
        topEnd = 6.dp,
        bottomStart = 6.dp,
        bottomEnd = 18.dp
    ),

    cardSymmetric = RoundedCornerShape(16.dp),

    buttonPrimary = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 4.dp,
        bottomEnd = 16.dp
    ),

    buttonSecondary = RoundedCornerShape(12.dp),

    chip = RoundedCornerShape(100.dp),

    badge = RoundedCornerShape(100.dp),

    searchBar = RoundedCornerShape(28.dp),

    bottomSheet = RoundedCornerShape(
        topStart = 28.dp,
        topEnd = 28.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    ),
)

val material3Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp),
)
