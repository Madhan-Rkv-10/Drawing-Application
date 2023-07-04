package com.example.drawingapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attributes: AttributeSet) : View(context, attributes) {
    private var myDrawPath: CustomPath? = null
    private var myCanvasBitmap: Bitmap? = null
    private var myDrawpaint: Paint? = null
    private var myCanvaspaint: Paint? = null
    private var mybrushSize: Float = 0.toFloat()
    private var canvas: Canvas? = null
    private var color = Color.BLACK

    init {
        setUpDrawing()
    }

    private fun setUpDrawing() {
        myDrawpaint = Paint()
        myDrawPath = CustomPath(color, mybrushSize)
        myDrawpaint!!.color = color
        myDrawpaint!!.style = Paint.Style.FILL_AND_STROKE
        myDrawpaint!!.strokeCap = Paint.Cap.ROUND
        myDrawpaint!!.strokeJoin = Paint.Join.ROUND
        myCanvaspaint = Paint(Paint.DITHER_FLAG)
        mybrushSize = 20.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        myCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(myCanvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(myCanvasBitmap!!, 0F, 0F, myCanvaspaint)
        myDrawPath.let {
            myDrawpaint?.strokeWidth = myDrawPath!!.brushThincknes
            myDrawpaint?.color = myDrawPath!!.color

            canvas.drawPath(myDrawPath!!, myDrawpaint!!)
        }
        canvas.drawPath(myDrawPath!!, myDrawpaint!!)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchx = event?.x
        val touchy = event?.y
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                myDrawPath!!.color = color
                myDrawPath!!.brushThincknes = mybrushSize
                myDrawPath!!.reset()
                if (touchx != null && touchy != null)
                    myDrawPath!!.moveTo(touchx, touchy)
            }
            MotionEvent.ACTION_MOVE -> {
                if (touchx != null && touchy != null)
                    myDrawPath!!.lineTo(touchx, touchy)
            }
            MotionEvent.ACTION_UP -> {
                myDrawPath = CustomPath(color, mybrushSize)
            }
            else -> return false

        }
        invalidate()
        return true
    }

    internal inner class CustomPath(var color: Int, var brushThincknes: Float) : Path() {

    }
}