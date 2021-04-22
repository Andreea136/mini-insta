package com.university.ip.ui.editor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.chrisbanes.photoview.PhotoView
import com.university.ip.R
import com.university.ip.ui.main.MainActivity
import com.university.ip.util.files.FileSaver.Companion.IMAGE_MIME_TYPE
import com.university.ip.util.files.FileSaverLegacy
import org.opencv.android.OpenCVLoader


class EditorActivity : AppCompatActivity(), EditorContract.View, View.OnClickListener,
    FiltersAdapter.ItemClickListener, SeekBar.OnSeekBarChangeListener {
    override fun appContext(): Context = applicationContext
    private val TAG = "EditorActivity"

    private lateinit var backButton: ImageView
    private lateinit var saveButton: TextView
    private lateinit var imageView: PhotoView


    private lateinit var filterList: RecyclerView
    private lateinit var seekBar: SeekBar
    private lateinit var seekBar2: SeekBar
    private lateinit var seekBar3: SeekBar

    private lateinit var fileSaver: FileSaverLegacy
    private lateinit var bitmap: Bitmap
    private lateinit var adapter: FiltersAdapter

    private lateinit var selectedFilter : String
    private lateinit var presenter: EditorPresenter

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        val layoutManager = LinearLayoutManager(appContext(), LinearLayoutManager.HORIZONTAL, false)
        filterList = findViewById(R.id.filters_list)
        filterList.layoutManager = layoutManager

        adapter = FiltersAdapter(appContext(), this)
        adapter.setMediaList(FILTERS_ARRAY)
        filterList.adapter = adapter

        seekBar = findViewById(R.id.seek_bar_editor)
        seekBar2 = findViewById(R.id.seek_bar_editor_2)
        seekBar3 = findViewById(R.id.seek_bar_editor_3)

        backButton = findViewById(R.id.back_editor)
        backButton.setOnClickListener(this)

        imageView = findViewById(R.id.image_edited)

        fileSaver = FileSaverLegacy(appContext())
        saveButton = findViewById(R.id.save_button)
        saveButton.setOnClickListener(this)
        presenter = EditorPresenter()
        presenter.bindView(this)

        //image load
        loadImage()
        openCvInit()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
    }

    private fun openCvInit(){
        if(!OpenCVLoader.initDebug()){
            Log.e(TAG, "OpenCV not loaded");
        } else {
            Log.e(TAG, "OpenCV loaded");
        }
    }

    private fun loadImage() {
        val data = intent.getBundleExtra(INTENT_EXTRAS)
        val requestCode = intent.getIntExtra(REQUEST_CODE, 2)
        val resultCode = intent.getIntExtra(RESULT_CODE, Activity.RESULT_CANCELED)

        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == Activity.RESULT_OK && intent != null) {
                    val selectedImage = data.get("data") as Bitmap
                    bitmap = selectedImage
                    imageView.setImageBitmap(selectedImage)
                }
                1 -> if (resultCode == Activity.RESULT_OK && intent != null) {
                    val selectedImage = intent.data!!
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(
                        selectedImage,
                        filePathColumn, null, null, null
                    )
                    if (cursor != null) {
                        cursor.moveToFirst()

                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val picturePath = cursor.getString(columnIndex)
                        bitmap = BitmapFactory.decodeFile(picturePath)
                        imageView.setImageBitmap(bitmap)
                        cursor.close()
                    }

                }
            }
        }
    }

    companion object {
        const val INTENT_EXTRAS: String = "INTENT_EXTRAS"
        const val REQUEST_CODE: String = "REQUEST_CODE"
        const val RESULT_CODE: String = "RESULT_CODE"
        val FILTERS_ARRAY : List<String> = listOf("Brightness", "Contrast", "Grayscale", "Binary Thresh",
            "Flip", "Rotate Clockwise", "Rotate Anticlockwise", "Gaussian Blur", "Median Blur", "Sobel", "Unsharp Mask", "Oil Painting", "Canny")
        val FILTERS_SLIDER_ARRAY : List<String> = listOf("Brightness", "Contrast", "Gaussian Blur","Binary Thresh", "Median Blur")

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_editor -> {
                finish()
            }
            R.id.save_button -> {
                val uri = fileSaver.getFileUri(IMAGE_MIME_TYPE) ?: return
                appContext().contentResolver.openOutputStream(uri)?.use { stream ->
                    imageView.drawToBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream)
                }
                startActivity(Intent(appContext(), MainActivity::class.java))
            }
        }
    }

    override fun onItemClick(filter: String) {
        selectedFilter = filter
        if(FILTERS_SLIDER_ARRAY.indexOf(selectedFilter) >= 0){

            seekBar2.visibility = View.GONE
            seekBar3.visibility = View.GONE

            seekBar.visibility = View.VISIBLE
            seekBar.setOnSeekBarChangeListener(this)

            if(FILTERS_SLIDER_ARRAY.indexOf(selectedFilter) < 3)
            {
                seekBar2.visibility = View.VISIBLE
                seekBar2.setOnSeekBarChangeListener(this)
            }

            if(FILTERS_SLIDER_ARRAY.indexOf(selectedFilter) < 2){
                seekBar3.visibility = View.VISIBLE
                seekBar3.setOnSeekBarChangeListener(this)
            }

        } else {
            seekBar.visibility = View.GONE
            seekBar2.visibility = View.GONE
            seekBar3.visibility = View.GONE

            when(FILTERS_ARRAY.indexOf(selectedFilter)){
                2 -> {
                    presenter.grayscale(bitmap)
                    return
                }
                4 -> {
                    presenter.flip(bitmap)
                    return
                }
                5 -> {
                    presenter.rotate(bitmap, true)
                    return
                }
                6 -> {
                    presenter.rotate(bitmap, false)
                    return
                }
                9 -> {
                    presenter.sobel(bitmap)
                    return
                }
                10 -> {
                    presenter.unsharpMask(bitmap)
                    return
                }
                11 -> {
                    presenter.oilPainting(bitmap)
                    return
                }
                12 -> {
                    presenter.canny(bitmap)
                    return
                }
                else -> return
            }

        }
        println(FILTERS_SLIDER_ARRAY.indexOf(selectedFilter))
    }

    override fun onProgressChanged( seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        println(progress)
        println(selectedFilter)
        when(FILTERS_SLIDER_ARRAY.indexOf(selectedFilter)){
            0 -> {
                when(seekBar.id){
                    R.id.seek_bar_editor ->{
                        presenter.brightness(bitmap, progress, 0)
                        return
                    }
                    R.id.seek_bar_editor_2 ->{
                        presenter.brightness(bitmap, progress, 1)
                        return
                    }
                    R.id.seek_bar_editor_3 ->{
                        presenter.brightness(bitmap, progress, 2)
                        return
                    }
                }
            }
            1 -> {
                when(seekBar.id){
                    R.id.seek_bar_editor ->{
                        presenter.contrast(bitmap, progress, 0)
                        return
                    }
                    R.id.seek_bar_editor_2 ->{
                        presenter.contrast(bitmap, progress, 1)
                        return
                    }
                    R.id.seek_bar_editor_3 ->{
                        presenter.contrast(bitmap, progress, 2)
                        return
                    }
                }
            }

            2 -> {
                when(seekBar.id){
                     R.id.seek_bar_editor ->{
                        presenter.gaussianBlur(bitmap, progress, 0)
                        return
                    }
                    R.id.seek_bar_editor_2 ->{
                        presenter.gaussianBlur(bitmap, progress, 1)
                        return
                    }
                }
            }

            3 -> {
                presenter.binary(bitmap, progress)
                return
            }
            4 -> {
                presenter.medianBlur(bitmap, progress)
                return
            }
            else -> return
        }

    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }

    override fun setBitmap(bitmap: Bitmap){
        imageView.setImageBitmap(bitmap)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }

}