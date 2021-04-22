package com.university.ip.repository.Operator

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc


class Operators {

    fun increaseBrightness(bitmap: Bitmap, value: Int, seekBar: Int) : Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        val dest: ArrayList<Mat> = ArrayList(3)
        Core.split(src, dest)
        when (seekBar) {
            0 -> {
                dest[0].convertTo(dest[0], -1, 1.0, value.toDouble())
            }
            1 -> {
                dest[1].convertTo(dest[1], -1, 1.0, value.toDouble())
            }
            else -> {
                dest[2].convertTo(dest[2], -1, 1.0, value.toDouble())
            }
        }

        Core.merge(dest, src)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)

        return result
    }

    fun increaseContrast(bitmap: Bitmap, value: Int, seekBar: Int) : Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        val dst: ArrayList<Mat> = ArrayList(3)
        Core.split(src, dst)
        when (seekBar) {
            0 -> {
                dst[0].convertTo(dst[0], -1, value.toDouble() / 2.0, 1.0)
            }
            1 -> {
                dst[1].convertTo(dst[1], -1, value.toDouble() / 2.0, 1.0)
            }
            else -> {
                dst[2].convertTo(dst[2], -1, value.toDouble() / 2.0, 1.0)
            }
        }

        Core.merge(dst, src)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun convertToGrayscale(bitmap: Bitmap) : Bitmap{
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2GRAY)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun binaryThreshold(bitmap: Bitmap, value: Int) : Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2GRAY)
        Imgproc.threshold(src, src, value.toDouble(), 255.0, Imgproc.THRESH_BINARY)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun flip(bitmap: Bitmap) : Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        Core.flip(src, src, -1)

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun rotate90(bitmap: Bitmap, value: Boolean) : Bitmap {
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        if(value == true)
            Core.rotate(src, src, Core.ROTATE_90_CLOCKWISE)
        else
            Core.rotate(src, src, Core.ROTATE_90_COUNTERCLOCKWISE)

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun gaussianBlur(bitmap: Bitmap, value: Int, seekBar: Int) : Bitmap{
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        if(seekBar == 0){
            var ksize = 3.0
            var margin = 10
            while(value >= margin)
            {
                ksize += 2.0
                margin += 10
            }
            if(value>10)
                Imgproc.GaussianBlur(src, src, Size(ksize, ksize), 0.0)
        }
        else
            Imgproc.GaussianBlur(src, src, Size(0.0, 0.0), value.toDouble() / 2.0)

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun medianBlur(bitmap: Bitmap, value: Int) : Bitmap{
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        var ksize = 3
        var margin = 10
        while(value >= margin)
        {
            ksize += 2
            margin += 10
        }

        if(value>10)
            Imgproc.medianBlur(src, src, ksize)

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun sobelX(bitmap: Bitmap) : Bitmap{
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2GRAY)
        Imgproc.Sobel(src, src, CvType.CV_8UC1, 1, 0)

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun unsharpMask(bitmap: Bitmap) : Bitmap{
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        val low = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, low)
        Imgproc.medianBlur(src, low, 5)

        Core.addWeighted(src, 1.5, low, -0.5, 1.0, src)

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun oilPainting(bitmap: Bitmap) : Bitmap{
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        var size = bitmap.height/25
        if(size%2==0)
            size+=1

        Imgproc.medianBlur(src, src, size)
        src.convertTo(src, -1, 1.0, 10.0)

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun canny(bitmap: Bitmap) : Bitmap{
        val src = Mat(bitmap.height, bitmap.width, CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        val gray = Mat(src.rows(), src.cols(), src.type())
        val edges = Mat(src.rows(), src.cols(), src.type())
        val dest = Mat(src.rows(), src.cols(), src.type())

        Imgproc.cvtColor(src, gray, Imgproc.COLOR_RGB2GRAY)
        Imgproc.blur(gray, edges, Size(3.0, 3.0))
        Imgproc.Canny(edges, edges, 25.0, (25 * 3).toDouble(), 3, false)
        src.copyTo(dest, edges)

        val result = Bitmap.createBitmap(dest.cols(), dest.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dest, result)
        return result
    }

}