package com.university.ip.ui.editor

import android.graphics.Bitmap
import androidx.core.graphics.set
import com.university.ip.repository.Operator.Operators
import com.university.ip.ui.base.BasePresenter

class EditorPresenter: BasePresenter<EditorContract.View>(), EditorContract.Presenter {
    private val operators = Operators()

    override fun brightness(bitmap: Bitmap, value: Int) {
        val result = operators.increaseBrightness(bitmap, value)
        getView()?.setBitmap(result)

    }

    override fun contrast(bitmap: Bitmap, value: Int) {
        val result = operators.increaseContrast(bitmap, value)
        getView()?.setBitmap(result)
    }

    override fun grayscale(bitmap: Bitmap) {
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




}
