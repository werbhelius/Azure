package com.werb.azure

import android.annotation.TargetApi
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment


/**
 * [AzureFragment] help to dispose permission
 * Created by wanbo on 2018/4/9.
 */
internal class AzureFragment : Fragment() {

    companion object {
        const val PERMISSIONS_REQUEST_CODE = 21
        const val PERMISSIONS_OVERLAY_PERMISSION = 22
        const val PERMISSIONS_WIRELESS_SETTINGS = 23
    }

    private var specialPermissions = mutableMapOf<String, Int>()
    private var dynamicPermissions = mutableListOf<String>()
    private lateinit var permissionViewModel: PermissionViewModel
    internal var grantedBlock: ((isGranted: Boolean) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            permissionViewModel = ViewModelProviders.of(it).get(PermissionViewModel::class.java)
            permissionViewModel.granted.observe(it, Observer {
                it?.let { granted ->
                    grantedBlock?.let { it(granted) }
                }
            })
        }
    }

    @Suppress("UNCHECKED_CAST")
    @TargetApi(Build.VERSION_CODES.M)
    internal fun requestAllPermissions(vararg permission: String) {
        dynamicPermissions = permission.toMutableList()
        specialPermissionCheck(dynamicPermissions)?.apply {
            specialPermissions = this
            forEachSpecialCheck()
        } ?: run {
            requestPermissions(dynamicPermissions.toTypedArray(), PERMISSIONS_REQUEST_CODE)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun forEachSpecialCheck() {
        specialPermissions.keys.forEach {
            when (specialPermissions[it]) {
                PERMISSIONS_OVERLAY_PERMISSION -> {
                    if (!Settings.canDrawOverlays(context)) {
                        requestSpecialPermission(it, specialPermissions[it] ?: return@forEach)
                        return
                    }
                }
                PERMISSIONS_WIRELESS_SETTINGS -> {
                    if (!Settings.System.canWrite(context)) {
                        requestSpecialPermission(it, specialPermissions[it] ?: return@forEach)
                        return
                    }
                }
            }
        }

        if (dynamicPermissions.isEmpty()) {
            permissionViewModel.granted.value = true
            return
        }
        requestPermissions(dynamicPermissions.toTypedArray(), PERMISSIONS_REQUEST_CODE)
    }

    // 申请危险权限
    private fun requestSpecialPermission(permission: String, requestCode: Int) {
        val intent = Intent(permission)
        intent.data = Uri.parse("package:${activity?.packageName}")
        startActivityForResult(intent, requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != PERMISSIONS_REQUEST_CODE) return
        permissionViewModel.granted.value = hasAllPermissionsGranted(grantResults)
    }


    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISSIONS_OVERLAY_PERMISSION) {
            if (Settings.canDrawOverlays(context)) {
                specialPermissions.remove(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                forEachSpecialCheck()
            } else {
                permissionViewModel.granted.value = false
            }
        }
        if (requestCode == PERMISSIONS_WIRELESS_SETTINGS) {
            if (Settings.System.canWrite(context)) {
                specialPermissions.remove(Settings.ACTION_WIRELESS_SETTINGS)
                forEachSpecialCheck()
            } else {
                permissionViewModel.granted.value = false
            }
        }
    }

    private fun hasAllPermissionsGranted(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }

    @TargetApi(Build.VERSION_CODES.M)
    internal fun checkPermissionsGranted(vararg permissions: String): Boolean {
        if (!isMarshmallow()) return true
        activity?.let { activity ->
            permissions.forEach {
                if (activity.checkSelfPermission(it) == PackageManager.PERMISSION_DENIED) return false
            }
            return true
        } ?: return false
    }

    private fun isMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

}