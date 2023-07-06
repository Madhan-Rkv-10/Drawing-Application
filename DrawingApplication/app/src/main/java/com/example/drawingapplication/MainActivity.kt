package com.example.drawingapplication

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import android.Manifest
import android.content.Intent
import android.provider.MediaStore
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    private var drawingView: DrawingView? = null
    private var myActiveColorImageButton: ImageButton? = null
    val requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            permission.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    Toast.makeText(
                        this@MainActivity,
                        "Permission granted now you can read the storage files.",
                        Toast.LENGTH_LONG
                    ).show()
                    val pickIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    galleryImageLauncher.launch(pickIntent)
                    //perform operation
                } else {
                    //Todo 4: Displaying another toast if permission is not granted and this time focus on
                    //    Read external storage
                    if (permissionName == Manifest.permission.READ_EXTERNAL_STORAGE)
                        Toast.makeText(
                            this@MainActivity,
                            "Oops you just denied the permission.",
                            Toast.LENGTH_LONG
                        ).show()
                }
            }
        }
    val galleryImageLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val imageBAckGround: ImageView = findViewById(R.id.iv_background)
                imageBAckGround.setImageURI(result.data?.data)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.drawing_view)
        drawingView?.setSizeForBrush(20.toFloat())
        val linearLayout = findViewById<LinearLayout>(R.id.color_pallete)
        myActiveColorImageButton = linearLayout[1] as ImageButton
        myActiveColorImageButton!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.pallet_pressed)
        )
        val ib_brush: ImageButton = findViewById(R.id.ib_brush)
        ib_brush.setOnClickListener {
            showSizeButtonDialog()
        }
        val ib_gallery = findViewById<ImageButton>(R.id.ic_gallery)
        ib_gallery.setOnClickListener {
            RequestStoragePermission()
        }

    }

    fun paintClicked(view: View) {
//        Toast.makeText(this,"Onclied",Toast.LENGTH_SHORT).show()
        if (view !== myActiveColorImageButton) {
            // Update the color
            val imageButton = view as ImageButton
            // Here the tag is used for swaping the current color with previous color.
            // The tag stores the selected view
            val colorTag = imageButton.tag.toString()
            // The color is set as per the selected tag here.
            drawingView?.setColor(colorTag)
            // Swap the backgrounds for last active and currently active image button.
            imageButton.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.pallet_pressed
                )
            )
            myActiveColorImageButton?.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.pallet_normal
                )
            )

            //Current view is updated with selected view in the form of ImageButton.
            myActiveColorImageButton = view
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

    private fun RequestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            showRationaleDialog(
                "Drawing APP",
                "Drawing App Need to Access your External Storage"
            )
        } else {
            requestPermission.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }

    private fun showRationaleDialog(
        title: String,
        message: String,
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}
