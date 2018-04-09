package com.werb.livepermissions.sample

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.werb.lifepermissions.LifePermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            LifePermissions(this)
                .request(Manifest.permission.CAMERA)
                .subscribe {
                    if (it) {
                        Toast.makeText(this, "CAMERA Permission ok", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "CAMERA Permission failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}
