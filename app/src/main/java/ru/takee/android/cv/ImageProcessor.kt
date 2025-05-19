package ru.takee.android.cv

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.nio.ByteBuffer
import java.nio.ByteOrder

object ImageProcessor {

    fun cropBitmap(originalBitmap: Bitmap, boxes: List<Float>): Bitmap?{
        // Проверяем, что boxes содержит ровно 4 элемента
        if (boxes.size != 4) return null

        val (centerX, centerY, width, height) = boxes
        val bitmapWidth = originalBitmap.width
        val bitmapHeight = originalBitmap.height

        // Преобразуем координаты из относительных (если они в [0, 1]) в абсолютные
        val absCenterX = if (centerX <= 1.0f) centerX * bitmapWidth else centerX
        val absCenterY = if (centerY <= 1.0f) centerY * bitmapHeight else centerY
        val absWidth = if (width <= 1.0f) width * bitmapWidth else width
        val absHeight = if (height <= 1.0f) height * bitmapHeight else height

        // Вычисляем координаты углов
        val halfWidth = absWidth / 2
        val halfHeight = absHeight / 2
        val left = (absCenterX - halfWidth).coerceIn(0f, bitmapWidth.toFloat()).toInt()
        val top = (absCenterY - halfHeight).coerceIn(0f, bitmapHeight.toFloat()).toInt()
        val right = (absCenterX + halfWidth).coerceIn(0f, bitmapWidth.toFloat()).toInt()
        val bottom = (absCenterY + halfHeight).coerceIn(0f, bitmapHeight.toFloat()).toInt()

        // Проверяем, что прямоугольник имеет положительную площадь
        if (left >= right || top >= bottom) return null

        return try {
            Bitmap.createBitmap(originalBitmap, left, top, right - left, bottom - top)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Основной процесс препроцессинга
    fun preprocessImage(originalBitmap: Bitmap, targetWidth: Int, targetHeight: Int, bgColor: Color): ByteBuffer {
        // Изменение размера с сохранением соотношения сторон
        val resizedBitmap = resizeImageWithAspectRatio(originalBitmap, targetWidth, targetHeight)

        // Добавление фона (padding)
        val paddedBitmap = addPadding(resizedBitmap, targetWidth, targetHeight, bgColor)

        // Нормализация изображения
        return normalizeImage(paddedBitmap)
    }

    // Функция для изменения размера изображения с сохранением соотношения сторон
    private fun resizeImageWithAspectRatio(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        val aspectRatio = Math.min(
            targetWidth.toFloat() / bitmap.width,
            targetHeight.toFloat() / bitmap.height
        )
        val newWidth = (bitmap.width * aspectRatio).toInt()
        val newHeight = (bitmap.height * aspectRatio).toInt()
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    // Функция для добавления фона (padding) до нужного размера
    private fun addPadding(bitmap: Bitmap, targetWidth: Int, targetHeight: Int, bgColor: Color): Bitmap {
        val resultBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)
        canvas.drawColor(bgColor.toArgb())
        val offsetX = (targetWidth - bitmap.width) / 2
        val offsetY = (targetHeight - bitmap.height) / 2
        canvas.drawBitmap(bitmap, offsetX.toFloat(), offsetY.toFloat(), null)
        return resultBitmap
    }

    // Функция для нормализации изображения (деление на 255)
    private fun normalizeImage(bitmap: Bitmap): ByteBuffer {
        val targetWidth = bitmap.width
        val targetHeight = bitmap.height
        val inputBuffer = ByteBuffer.allocateDirect(1 * targetHeight * targetWidth * 3 * 4) // float32 = 4 байта
        inputBuffer.order(ByteOrder.nativeOrder())

        for (y in 0 until targetHeight) {
            for (x in 0 until targetWidth) {
                val pixel = bitmap.getPixel(x, y)
                val r = ((pixel shr 16 and 0xFF) / 255.0f)
                val g = ((pixel shr 8 and 0xFF) / 255.0f)
                val b = ((pixel and 0xFF) / 255.0f)
                inputBuffer.putFloat(r)
                inputBuffer.putFloat(g)
                inputBuffer.putFloat(b)
            }
        }

        return inputBuffer
    }

}