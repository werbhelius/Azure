package com.werb.azure.sample

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.werb.azure.Azure
import com.werb.pickphotoview.PickPhotoView
import com.werb.pickphotoview.util.PickConfig
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.GridLayoutManager
import com.werb.library.MoreAdapter
import com.werb.library.link.RegisterItem


class MainActivity : AppCompatActivity() {

    private val adapter = MoreAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = GridLayoutManager(this, 4)
        recyclerView.layoutManager = layoutManager
        adapter.apply {
            register(RegisterItem(R.layout.layout_image, ImageHolder::class.java))
            attachTo(recyclerView)
        }

        button.setOnClickListener {
            requestStoragePermissions()
        }

        button2.setOnClickListener {
            requestCameraPermissions()
        }
    }

    private fun requestStoragePermissions() {
        Azure(this)
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe {
                if (it) {
                    Toast.makeText(this, "Storage Permissions Granted", Toast.LENGTH_SHORT).show()
                    openAlbum()
                } else {
                    permissionDialog()
                }
            }.request()
    }

    private fun requestCameraPermissions() {
        Azure(this)
            .permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe {
                if (it) {
                    Toast.makeText(this, "Camera Permissions Granted", Toast.LENGTH_SHORT).show()
                    startActivityForResult(Intent(this, CameraActivity::class.java), PickConfig.PICK_PHOTO_DATA)
                } else {
                    permissionDialog()
                }
            }.request()
    }

    private fun openAlbum() {
        PickPhotoView.Builder(this)
            .setPickPhotoSize(9)                  // select image size
            .setShowCamera(true)                  // is show camera
            .setSpanCount(4)                      // span count
            .setLightStatusBar(true)              // lightStatusBar used in Android M or higher
            .setStatusBarColor(R.color.colorWhite)     // statusBar color
            .setToolbarColor(R.color.colorWhite)       // toolbar color
            .setToolbarTextColor(R.color.colorBlack)   // toolbar text color
            .setSelectIconColor(R.color.colorAccent)     // select icon color
            .setShowGif(true)                    // is show gif
            .start()
    }

    private fun permissionDialog() {
        AlertDialog.Builder(this)
            .setTitle("We need all the permissions to keep running.")
            .setNegativeButton("dismiss") { dialog, _ -> dialog?.dismiss() }
            .setPositiveButton("go setting") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
            }.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0) {
            return
        }
        if (data == null) {
            return
        }
        if (requestCode == PickConfig.PICK_PHOTO_DATA) {
            @Suppress("UNCHECKED_CAST")
            val selectPaths = data.getSerializableExtra(PickConfig.INTENT_IMG_LIST_SELECT) as ArrayList<String>
            // do something u want
            adapter.loadData(selectPaths)

        }
    }
}
