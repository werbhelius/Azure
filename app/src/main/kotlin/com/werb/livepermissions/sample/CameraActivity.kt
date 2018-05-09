package com.werb.livepermissions.sample

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import com.otaliastudios.cameraview.CameraListener
import com.werb.pickphotoview.util.PickConfig
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.util.*

/**
 * Created by wanbo on 2018/5/9.
 */
class CameraActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        click.setOnClickListener {
            camera.capturePicture()
        }

        camera.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(jpeg: ByteArray?) {
                jpeg?.let {
                    val file = File(saveImageDir, UUID.randomUUID().toString() + ".jpg")
                    file.writeBytes(it)
                    val intent = Intent()
                    intent.putExtra(PickConfig.INTENT_IMG_LIST_SELECT, arrayListOf(file.absolutePath))
                    setResult(PickConfig.PICK_PHOTO_DATA, intent)
                    finish()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        camera.start()
    }

    override fun onPause() {
        super.onPause()
        camera.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        camera.destroy()
    }

    private fun createExternalDir(directory: String): File? {
        val createDir = File(Environment.getExternalStorageDirectory().toString() + File.separator + directory)
        if (!createDir.exists()) {
            if (createDir.mkdirs()) {
                return createDir
            }
        } else {
            return createDir
        }
        return null
    }

    val saveImageDir: File?
        get() = createExternalDir(packageName + File.separator + "Images")

}