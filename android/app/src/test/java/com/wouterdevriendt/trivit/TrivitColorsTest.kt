package com.wouterdevriendt.trivit

import com.wouterdevriendt.trivit.ui.theme.TrivitColors
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class TrivitColorsTest {

    @Test
    fun `vibrant palette has 10 colors`() {
        assertEquals(10, TrivitColors.vibrant.colors.size)
        assertEquals(10, TrivitColors.vibrant.darkColors.size)
    }

    @Test
    fun `all palettes have 10 colors`() {
        TrivitColors.allPalettes.forEach { palette ->
            assertEquals("${palette.name} should have 10 colors", 10, palette.colors.size)
            assertEquals("${palette.name} should have 10 dark colors", 10, palette.darkColors.size)
        }
    }

    @Test
    fun `there are 8 color schemes`() {
        assertEquals(8, TrivitColors.allPalettes.size)
    }

    @Test
    fun `getColor wraps index correctly`() {
        val color0 = TrivitColors.getColor(0, 0)
        val color10 = TrivitColors.getColor(0, 10)
        assertEquals(color0, color10)
    }

    @Test
    fun `getPalette clamps index`() {
        val palette = TrivitColors.getPalette(100)
        assertNotNull(palette)
        assertEquals(TrivitColors.allPalettes.last(), palette)
    }

    @Test
    fun `getPalette negative index clamps to 0`() {
        val palette = TrivitColors.getPalette(-1)
        assertEquals(TrivitColors.allPalettes.first(), palette)
    }

    @Test
    fun `getDarkColor returns dark variant`() {
        val darkColor = TrivitColors.getDarkColor(0, 0)
        assertEquals(TrivitColors.TurquoiseDark, darkColor)
    }

    @Test
    fun `palette names are correct`() {
        val names = TrivitColors.allPalettes.map { it.name }
        assertEquals(
            listOf("Vibrant", "Pastel", "Monochrome", "Ocean", "Sunset", "Forest", "Candy", "Earth"),
            names
        )
    }
}
