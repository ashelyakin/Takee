package ru.takee.android.color

import android.util.Log
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlin.math.pow
import kotlin.math.sqrt

object ColorUtils {

    fun findClosestColor(color: Int): ColorsValue? {
        try {
            val dests = ColorsValue.values().map {
                findDest(color, it.color)
            }
            val index = dests.indexOf(dests.minOrNull())
            return ColorsValue.values()[index]
        } catch (e: Exception){
            Log.e("ColorUtils", e.stackTraceToString())
            return null
        }
    }

    private fun findDest(color1: Int, color2: Int): Float {
        return sqrt(
            ((color1.red - color2.red).toFloat()).pow(2)
                    + ((color1.green - color2.green).toFloat()).pow(2)
                    + ((color1.blue - color2.blue).toFloat()).pow(2)
        )
    }

}