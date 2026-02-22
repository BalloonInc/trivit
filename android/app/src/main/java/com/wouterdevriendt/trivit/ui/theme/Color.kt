package com.wouterdevriendt.trivit.ui.theme

import androidx.compose.ui.graphics.Color

data class TrivitColorPalette(
    val name: String,
    val colors: List<Color>,
    val darkColors: List<Color>
)

object TrivitColors {

    // Vibrant palette
    val Turquoise = Color(0xFF1ABC9C)
    val Emerald = Color(0xFF2ECC71)
    val PeterRiver = Color(0xFF3498DB)
    val Amethyst = Color(0xFF9B59B6)
    val Alizarin = Color(0xFFE74C3C)
    val Orange = Color(0xFFF39C12)
    val Pink = Color(0xFFE91E63)
    val Cyan = Color(0xFF00BCD4)
    val LightGreen = Color(0xFF8BC34A)
    val DeepOrange = Color(0xFFFF5722)

    // Dark variants
    val TurquoiseDark = Color(0xFF16A085)
    val EmeraldDark = Color(0xFF27AE60)
    val PeterRiverDark = Color(0xFF2980B9)
    val AmethystDark = Color(0xFF8E44AD)
    val AlizarinDark = Color(0xFFC0392B)
    val OrangeDark = Color(0xFFD68910)
    val PinkDark = Color(0xFFC2185B)
    val CyanDark = Color(0xFF0097A7)
    val LightGreenDark = Color(0xFF689F38)
    val DeepOrangeDark = Color(0xFFE64A19)

    val vibrant = TrivitColorPalette(
        name = "Vibrant",
        colors = listOf(Turquoise, Emerald, PeterRiver, Amethyst, Alizarin, Orange, Pink, Cyan, LightGreen, DeepOrange),
        darkColors = listOf(TurquoiseDark, EmeraldDark, PeterRiverDark, AmethystDark, AlizarinDark, OrangeDark, PinkDark, CyanDark, LightGreenDark, DeepOrangeDark)
    )

    val pastel = TrivitColorPalette(
        name = "Pastel",
        colors = listOf(
            Color(0xFFFFB3BA), Color(0xFFFFDFBA), Color(0xFFFFFFBA), Color(0xFFBAFFC9),
            Color(0xFFBAE1FF), Color(0xFFD4BAFF), Color(0xFFFFBAE1), Color(0xFFBAFFFF),
            Color(0xFFE1FFBA), Color(0xFFFFC9BA)
        ),
        darkColors = listOf(
            Color(0xFFE89AA3), Color(0xFFE8C8A3), Color(0xFFE8E8A3), Color(0xFFA3E8B2),
            Color(0xFFA3CAE8), Color(0xFFBDA3E8), Color(0xFFE8A3CA), Color(0xFFA3E8E8),
            Color(0xFFCAE8A3), Color(0xFFE8B2A3)
        )
    )

    val monochrome = TrivitColorPalette(
        name = "Monochrome",
        colors = listOf(
            Color(0xFF2C3E50), Color(0xFF34495E), Color(0xFF7F8C8D), Color(0xFF95A5A6),
            Color(0xFFBDC3C7), Color(0xFF5D6D7E), Color(0xFF85929E), Color(0xFFAAB7B8),
            Color(0xFF4A6274), Color(0xFF6E7B8B)
        ),
        darkColors = listOf(
            Color(0xFF1A252F), Color(0xFF222D38), Color(0xFF616E6F), Color(0xFF748586),
            Color(0xFF99A3A4), Color(0xFF465566), Color(0xFF6B7B8D), Color(0xFF8E9B9C),
            Color(0xFF384D5E), Color(0xFF566573)
        )
    )

    val ocean = TrivitColorPalette(
        name = "Ocean",
        colors = listOf(
            Color(0xFF0077B6), Color(0xFF00B4D8), Color(0xFF0096C7), Color(0xFF48CAE4),
            Color(0xFF90E0EF), Color(0xFF023E8A), Color(0xFF0353A4), Color(0xFF006494),
            Color(0xFFADE8F4), Color(0xFF03045E)
        ),
        darkColors = listOf(
            Color(0xFF005A8A), Color(0xFF0092AD), Color(0xFF00749B), Color(0xFF35A7BE),
            Color(0xFF6DB8C9), Color(0xFF012D66), Color(0xFF023C7E), Color(0xFF004B70),
            Color(0xFF8CBFC4), Color(0xFF020340)
        )
    )

    val sunset = TrivitColorPalette(
        name = "Sunset",
        colors = listOf(
            Color(0xFFFF6B6B), Color(0xFFFF8E72), Color(0xFFFFA07A), Color(0xFFFFB347),
            Color(0xFFFFD700), Color(0xFFFF4757), Color(0xFFFF6348), Color(0xFFFF7F50),
            Color(0xFFFFC312), Color(0xFFEE5A24)
        ),
        darkColors = listOf(
            Color(0xFFD44B4B), Color(0xFFD46E52), Color(0xFFD4805A), Color(0xFFD49327),
            Color(0xFFD4B700), Color(0xFFD42737), Color(0xFFD44328), Color(0xFFD45F30),
            Color(0xFFD4A300), Color(0xFFC43A04)
        )
    )

    val forest = TrivitColorPalette(
        name = "Forest",
        colors = listOf(
            Color(0xFF2D6A4F), Color(0xFF40916C), Color(0xFF52B788), Color(0xFF74C69D),
            Color(0xFF95D5B2), Color(0xFF1B4332), Color(0xFF2D6A4F), Color(0xFF344E41),
            Color(0xFFB7E4C7), Color(0xFF588157)
        ),
        darkColors = listOf(
            Color(0xFF1E4D38), Color(0xFF2D6F4F), Color(0xFF3D956D), Color(0xFF58A67F),
            Color(0xFF73B592), Color(0xFF0F2D20), Color(0xFF1E4D38), Color(0xFF243828),
            Color(0xFF93C4A1), Color(0xFF3E623D)
        )
    )

    val candy = TrivitColorPalette(
        name = "Candy",
        colors = listOf(
            Color(0xFFFF69B4), Color(0xFFFF1493), Color(0xFFDA70D6), Color(0xFFBA55D3),
            Color(0xFFFF6EB4), Color(0xFFFF82AB), Color(0xFFEE82EE), Color(0xFFDDA0DD),
            Color(0xFFFF00FF), Color(0xFFC71585)
        ),
        darkColors = listOf(
            Color(0xFFD44D95), Color(0xFFD40076), Color(0xFFB554B1), Color(0xFF9A3AB1),
            Color(0xFFD44D95), Color(0xFFD4648B), Color(0xFFC664C6), Color(0xFFB880B8),
            Color(0xFFD400D4), Color(0xFFA50F69)
        )
    )

    val earth = TrivitColorPalette(
        name = "Earth",
        colors = listOf(
            Color(0xFF8B4513), Color(0xFFA0522D), Color(0xFFCD853F), Color(0xFFD2691E),
            Color(0xFFDEB887), Color(0xFF6B4226), Color(0xFF8B6914), Color(0xFFA67B5B),
            Color(0xFFC4A882), Color(0xFF795548)
        ),
        darkColors = listOf(
            Color(0xFF6B3410), Color(0xFF803E22), Color(0xFFA56A30), Color(0xFFAA5017),
            Color(0xFFB89868), Color(0xFF4D301B), Color(0xFF6B500F), Color(0xFF845F44),
            Color(0xFF9E8766), Color(0xFF5D4037)
        )
    )

    val allPalettes = listOf(vibrant, pastel, monochrome, ocean, sunset, forest, candy, earth)

    fun getPalette(index: Int): TrivitColorPalette {
        return allPalettes[index.coerceIn(0, allPalettes.lastIndex)]
    }

    fun getColor(paletteIndex: Int, colorIndex: Int): Color {
        val palette = getPalette(paletteIndex)
        return palette.colors[colorIndex % palette.colors.size]
    }

    fun getDarkColor(paletteIndex: Int, colorIndex: Int): Color {
        val palette = getPalette(paletteIndex)
        return palette.darkColors[colorIndex % palette.darkColors.size]
    }

    fun getLightColor(paletteIndex: Int, colorIndex: Int): Color {
        val color = getColor(paletteIndex, colorIndex)
        return color.copy(alpha = 0.3f)
    }
}
