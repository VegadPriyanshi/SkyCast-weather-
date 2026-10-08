package com.pv.skycastweather.utils

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.net.URL

object ImageCacheHelper {

    fun downloadAndSaveImage(
        context: Context,
        imageUrl: String,
        fileName: String
    ): String? {

        return try {

            val inputStream = URL(imageUrl).openStream()

            val file = File(
                context.filesDir,
                fileName
            )

            val outputStream =
                FileOutputStream(file)

            inputStream.copyTo(outputStream)

            outputStream.close()
            inputStream.close()

            file.absolutePath

        } catch (e: Exception) {

            e.printStackTrace()

            null

        }

    }

}