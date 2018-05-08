package com.werb.livepermissions.sample

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.werb.lifepermissions.LifePermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            requestPermissions()
        }

        button2.setOnClickListener {
            requestPermissions2()
        }
    }

    private fun requestPermissions() {
        LifePermissions(this)
            .permissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe {
                if (it) {
                    Toast.makeText(this, "All Permissions Granted", Toast.LENGTH_SHORT).show()
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("We need all the permissions to keep running.")
                        .setNegativeButton("dismiss") { dialog, _ -> dialog?.dismiss() }
                        .setPositiveButton("go setting") { _, _ ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.data = Uri.fromParts("package", packageName, null)
                            startActivity(intent)
                        }.create().show()
                }
            }
            .request()
    }

    private fun requestPermissions2() {
        LifePermissions(this)
            .permissions(Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe {
                if (it) {
                    Toast.makeText(this, "All Permissions Granted", Toast.LENGTH_SHORT).show()
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("We need all the permissions to keep running.")
                        .setNegativeButton("dismiss") { dialog, _ -> dialog?.dismiss() }
                        .setPositiveButton("go setting") { _, _ ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.data = Uri.fromParts("package", packageName, null)
                            startActivity(intent)
                        }.create().show()
                }
            }
            .request()
    }
}
