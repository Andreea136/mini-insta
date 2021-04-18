package com.university.ip.repository.Operator

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat

class Operators {

    fun increaseBrightness(bitmap: Bitmap, value: Int) : Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        src.convertTo(src, -1,1.0, value.toDouble())
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun increaseContrast(bitmap: Bitmap, value: Int) : Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        src.convertTo(src, -1, value.toDouble(),1.0)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    //pt decrease 1 in loc de -1
    //contrast 0-10
    //brightness 0=200
}