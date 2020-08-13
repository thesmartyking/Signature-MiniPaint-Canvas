package com.example.canvasminipaint

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.media.MediaScannerConnection
import android.os.Environment
import android.os.Environment.DIRECTORY_DOCUMENTS
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.drawToBitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


private const val STROKE_WIDTH = 15f


class CanvasView(context:Context): View(context) {


    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var currentX = 0f
    private var currentY = 0f
    private lateinit var frame: Rect
    lateinit var pathdirect:String
    private val IMAGE_DIRECTORY = "/signaturecanvasappnew"
    private val drawColor = ResourcesCompat.getColor(resources, R.color.colorPaint, null)
    private val drawColor1 = ResourcesCompat.getColor(resources, R.color.colorPaint2, null)
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)
    private var path = Path()


    private val paint = Paint().apply {
        color = drawColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = STROKE_WIDTH
    }
    private val paint2=Paint().apply{
        color = drawColor1
        textSize= 100F
        style=Paint.Style.FILL
        //set.toString(displayText)


    }
    private val paint3=Paint().apply{
        color = drawColor1
        textSize= 50F
        style=Paint.Style.FILL



    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)


        val inset = 5
        frame = Rect(inset, inset, width - inset, height - inset)

    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
        extraCanvas.drawRect(frame, paint)

    }
    fun reseter(canvas: Canvas)
    {
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
        extraCanvas.drawRect(frame, paint)
        extraCanvas.drawColor(backgroundColor)
    }
    fun cleared()
    {
        //reseter(canvas)
        reseter(extraCanvas)
        invalidate()
        Log.d("TAG","reset")
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }
    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }


    private fun touchMove() {
        val dx = Math.abs(motionTouchEventX - currentX)
        val dy = Math.abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            extraCanvas.drawPath(path, paint)
        }
        invalidate()
    }

    private fun touchUp() {
        path.reset()
    }

    fun saveitfun() {

        val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        this.draw(canvas)
        pathdirect = saveImage(bitmap)
    }
    @SuppressLint("WrongThread")
    fun saveImage(myBitmap:Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val wallpaperDirectory = File("/storage/emulated/0/" + IMAGE_DIRECTORY)
        if(!wallpaperDirectory.exists()){
            wallpaperDirectory.mkdirs()
            Log.d("Directories Creation","Direction created")
            //wallpaperDirectory.mkdirs()
            Toast.makeText(context, "Image Saved --> Check Directory", Toast.LENGTH_SHORT).show()
            Log.d("image", wallpaperDirectory.toString())
        }
        Log.d("TAG", "Wallpaper Path: $wallpaperDirectory or ${Environment.getRootDirectory().toString() + DIRECTORY_DOCUMENTS}")
//            Toast.makeText(this@MainActivity, getString(wallpaperDirectory), Toast.LENGTH_SHORT).show()\
        try
        {
            val fileName = "CMP--" +Calendar.getInstance() .getTimeInMillis().toString() + ".jpg"
            Log.d("file","fffff-->"+fileName)
            val f = File(wallpaperDirectory, fileName)
            f.createNewFile()
            Toast.makeText(context, "Image Saved --> Check Directory \n" +wallpaperDirectory+"\n"+ fileName, Toast.LENGTH_SHORT).show()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
                Log.d("File Generated","File has been generated and saved")
                MediaScannerConnection.scanFile(context, arrayOf<String>(f.getPath()), arrayOf<String>("image/jpeg"), null)
                fo.close()
                Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())
            return f.getAbsolutePath()

        }
        catch (e1: IOException) {
            e1.printStackTrace()
            Toast.makeText(
                context,
                "Permission Denied, So Give Permission From App info --> Permission --> Turn ON ",
                Toast.LENGTH_SHORT
            ).show()
            Log.d("Error", "$e1")
        }
        return ""
    }
}


