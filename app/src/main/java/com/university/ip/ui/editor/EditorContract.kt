package com.university.ip.ui.editor

import android.graphics.Bitmap
import com.university.ip.ui.base.BaseContract

interface EditorContract {

    interface View : BaseContract.View {
        //view functions for each change of activity
        fun setBitmap(bitmap: Bitmap)
    }

    interface Presenter {
        //functions that are going to use our library

        fun brightness(bitmap: Bitmap, value: Int, seekBar: Int)

        fun contrast(bitmap: Bitmap, value: Int, seekBar: Int)

        fun grayscale(bitmap: Bitmap)

        fun binary(bitmap: Bitmap, value: Int)

        fun flip(bitmap: Bitmap)

        fun rotate(bitmap: Bitmap, value: Boolean)

        fun gaussianBlur(bitmap: Bitmap, value: Int, seekBar: Int)

        fun medianBlur(bitmap: Bitmap, value: Int)

        fun sobel(bitmap: Bitmap)

        fun unsharpMask(bitmap: Bitmap)

        fun oilPainting(bitmap: Bitmap)

        fun canny(bitmap: Bitmap)
    }
}