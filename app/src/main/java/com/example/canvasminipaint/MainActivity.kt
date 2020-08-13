package com.example.canvasminipaint

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {


    lateinit var clearButton: Button
    lateinit var saveButton: Button
    lateinit var linear:LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clearButton = findViewById<Button>(R.id.clearit)
        saveButton = findViewById<Button>(R.id.saveit)
        linear =findViewById<LinearLayout>(R.id.linear1)
        val myCanvasView = CanvasView(this)
        linear.addView(myCanvasView)

        clearButton.setOnClickListener(View.OnClickListener {
            myCanvasView.cleared()
            Toast.makeText(this@MainActivity, "Image Cleared", Toast.LENGTH_SHORT).show()
//            Toast.makeText(this@MainActivity, "Image Saved --> Check Directory", Toast.LENGTH_SHORT).show()
        })

        saveButton.setOnClickListener (View.OnClickListener{
            myCanvasView.saveitfun()
//            Toast.makeText(this@MainActivity, "Image Saved --> Check Directory", Toast.LENGTH_SHORT).show()

        })
        if (ContextCompat.checkSelfPermission(this,WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)) {
            } else {

                ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE),1)

            }
        } else {
            // Permission has already been granted
        }


    }




}


