package com.example.drawingapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
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
    private val myPath = ArrayList<CustomPath>()
    private val myUndoPath = ArrayList<CustomPath>()

    init {
        setUpDrawing()
    }

    fun onCLickUndo() {
        if (myPath.size > 0) {
            myUndoPath.add(
                myPath.removeAt(myPath.size - 1)
            )
            invalidate()
        }
    }

    private fun setUpDrawing() {
        myDrawpaint = Paint()
        myDrawPath = CustomPath(color, mybrushSize)
        myDrawpaint!!.color = color
        myDrawpaint!!.style = Paint.Style.STROKE
        myDrawpaint!!.strokeCap = Paint.Cap.ROUND
        myDrawpaint!!.strokeJoin = Paint.Join.ROUND
        myCanvaspaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        myCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(myCanvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(myCanvasBitmap!!, 0F, 0F, myCanvaspaint)

        for (path in myPath) {
            myDrawpaint?.strokeWidth = path.brushThincknes
            myDrawpaint?.color = path.color
            canvas.drawPath(path, myDrawpaint!!)
        }
        myDrawPath.let {
            myDrawpaint?.strokeWidth = myDrawPath!!.brushThincknes
            myDrawpaint?.color = myDrawPath!!.color

            canvas.drawPath(myDrawPath!!, myDrawpaint!!)
        }
        canvas.drawPath(myDrawPath!!, myDrawpaint!!)
    }

    fun setColor(newColor: String) {
        color = Color.parseColor(newColor)
        myDrawpaint?.color = color
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
                myPath.add(myDrawPath!!)
                myDrawPath = CustomPath(color, mybrushSize)
            }
            else -> return false

        }
        invalidate()
        return true
    }

    fun setSizeForBrush(newSize: Float) {
        mybrushSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            newSize,
            resources.displayMetrics
        )
        myDrawpaint!!.strokeWidth = mybrushSize
    }

    internal inner class CustomPath(var color: Int, var brushThincknes: Float) : Path() {

    }
}