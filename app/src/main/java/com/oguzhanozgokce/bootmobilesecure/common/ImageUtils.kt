package com.oguzhanozgokce.bootmobilesecure.common

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.UUID

object ImageUtils {

    /**
     * Creates a temporary file from URI for upload
     */
    fun createTempFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val contentResolver: ContentResolver = context.contentResolver
            val inputStream: InputStream? = contentResolver.openInputStream(uri)

            inputStream?.use { stream ->
                // Get file extension from MIME type
                val mimeType = contentResolver.getType(uri)
                val extension = when (mimeType) {
                    "image/jpeg", "image/jpg" -> "jpg"
                    "image/png" -> "png"
                    "image/gif" -> "gif"
                    else -> "jpg"
                }

                // Create temp file
                val tempFile = File(context.cacheDir, "profile_image_${UUID.randomUUID()}.$extension")

                // Copy stream to file
                FileOutputStream(tempFile).use { outputStream ->
                    stream.copyTo(outputStream)
                }

                tempFile
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Compresses image if it's too large
     */
    fun compressImageIfNeeded(file: File, maxSizeBytes: Long = 20 * 1024 * 1024): File? {
        return try {
            if (file.length() <= maxSizeBytes) {
                return file
            }

            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val ratio = kotlin.math.sqrt(maxSizeBytes.toDouble() / file.length())
            val newWidth = (bitmap.width * ratio).toInt()
            val newHeight = (bitmap.height * ratio).toInt()

            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)

            // Create compressed file
            val compressedFile = File(file.parent, "compressed_${file.name}")
            FileOutputStream(compressedFile).use { outputStream ->
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            }

            // Clean up
            bitmap.recycle()
            scaledBitmap.recycle()
            file.delete()

            compressedFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Validates image file
     */
    fun isValidImageFile(file: File): Boolean {
        val validExtensions = listOf("jpg", "jpeg", "png", "gif")
        val extension = file.extension.lowercase()
        return validExtensions.contains(extension) && file.exists() && file.length() > 0
    }

    /**
     * Gets MIME type from file extension
     */
    fun getMimeType(file: File): String {
        return when (file.extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            else -> "image/jpeg"
        }
    }

    /**
     * Cleans up temporary files
     */
    fun cleanupTempFile(file: File?) {
        try {
            file?.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}