package com.wouterdevriendt.trivit.service

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HapticsService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            manager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    fun tick() {
        vibrate(VibrationEffect.EFFECT_TICK)
    }

    fun lightImpact() {
        vibrate(VibrationEffect.EFFECT_CLICK)
    }

    fun mediumImpact() {
        vibrate(VibrationEffect.EFFECT_HEAVY_CLICK)
    }

    fun error() {
        vibrate(VibrationEffect.EFFECT_DOUBLE_CLICK)
    }

    private fun vibrate(effectId: Int) {
        vibrator?.let {
            if (it.hasVibrator()) {
                it.vibrate(VibrationEffect.createPredefined(effectId))
            }
        }
    }
}
