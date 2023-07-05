package com.example.drawingapplication

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    private var drawingView: DrawingView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.drawing_view)
        drawingView?.setSizeForBrush(20.toFloat())
        val ib_brush: ImageButton = findViewById(R.id.ib_brush)
        ib_brush.setOnClickListener {
            showSizeButtonDialog()
        }

    }

    private fun showSizeButtonDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.brush_size_dialogue)

        brushDialog.setTitle("Brush Size:")
        val btnSmall = brushDialog.findViewById<ImageButton>(R.id.ib_small)

//
        btnSmall.setOnClickListener {
            drawingView?.setSizeForBrush(10.toFloat())
            brushDialog.dismiss()
        }
        val btnMedium = brushDialog.findViewById<ImageButton>(R.id.ib_medium)

//
        btnMedium.setOnClickListener {
            drawingView?.setSizeForBrush(20.toFloat())
            brushDialog.dismiss()
        }
        val btnLarge = brushDialog.findViewById<ImageButton>(R.id.ib_large)

//
        btnLarge.setOnClickListener {
            drawingView?.setSizeForBrush(30.toFloat())
            brushDialog.dismiss()
        }
        brushDialog.show()
    }
}
