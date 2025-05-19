package ru.takee.android.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore

object BitmapUtils {

    fun modifyOrientation(bitmap: Bitmap, uri: Uri, contentResolver: ContentResolver): Bitmap {
        return try {
            rotate(bitmap, getOrientation(uri, contentResolver).toFloat())
        }catch (e: Exception) {
            bitmap
        }
    }

    private fun getOrientation(photoUri: Uri, contentResolver: ContentResolver): Int {
        val cursor = contentResolver.query(photoUri, arrayOf(MediaStore.Images.ImageColumns.ORIENTATION), null, null, null)
        if (cursor == null || cursor.count != 1) {
            return 90
        }
        cursor.moveToFirst()
        val res = cursor.getInt(0)
        cursor.close()
        return res
    }

    private fun rotate(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

}