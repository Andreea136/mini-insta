package com.university.ip.ui.editor

import android.graphics.Bitmap
import com.university.ip.repository.Operator.Operators
import com.university.ip.ui.base.BasePresenter

class EditorPresenter: BasePresenter<EditorContract.View>(), EditorContract.Presenter {
    private val operators = Operators()

    override fun brightness(bitmap: Bitmap, value: Int, seekBar: Int){
        val result = operators.increaseBrightness(bitmap, value, seekBar)
        getView()?.setBitmap(result)
    }

    override fun contrast(bitmap: Bitmap, value: Int, seekBar: Int){
        val result = operators.increaseContrast(bitmap, value, seekBar)
        getView()?.setBitmap(result)
    }

    override fun grayscale(bitmap: Bitmap){
        val result = operators.convertToGrayscale(bitmap)
        getView()?.setBitmap(result)
    }

    override fun binary(bitmap: Bitmap, value: Int){
        val result = operators.binaryThreshold(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun flip(bitmap: Bitmap){
        val result = operators.flip(bitmap)
        getView()?.setBitmap(result)
    }

    override fun rotate(bitmap: Bitmap, value: Boolean){
        val result = operators.rotate90(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun gaussianBlur(bitmap: Bitmap, value: Int, seekBar: Int){
        val result = operators.gaussianBlur(bitmap, value, seekBar)
        getView()?.setBitmap(result)
    }

    override fun medianBlur(bitmap: Bitmap, value: Int){
        val result = operators.medianBlur(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun sobel(bitmap: Bitmap){
        val result = operators.sobelX(bitmap)
        getView()?.setBitmap(result)
    }

    override fun unsharpMask(bitmap: Bitmap){
        val result = operators.unsharpMask(bitmap)
        getView()?.setBitmap(result)
    }

    override fun oilPainting(bitmap: Bitmap){
        val result = operators.oilPainting(bitmap)
        getView()?.setBitmap(result)
    }

    override fun canny(bitmap: Bitmap){
        val result = operators.canny(bitmap)
        getView()?.setBitmap(result)
    }



}
