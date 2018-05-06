package com.werb.lifepermissions

import android.annotation.TargetApi
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment


/**
 * [LifePermissionFragment] help to dispose permission
 * Created by wanbo on 2018/4/9.
 */
internal class LifePermissionFragment : Fragment() {

    private val PERMISSIONS_REQUEST_CODE = 21
    private lateinit var permissionViewModel: PermissionViewModel
    private lateinit var block: (granted: Boolean) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            permissionViewModel = ViewModelProviders.of(it).get(PermissionViewModel::class.java)
            permissionViewModel.granted.observe(it, Observer {
                it?.let {
                    block(it)
                }
            })
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    internal fun requestAllPermissions(vararg permission: String, block: (granted: Boolean) -> Unit) {
        this.block = block
        requestPermissions(permission, PERMISSIONS_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != PERMISSIONS_REQUEST_CODE) return
        // 表示用户是否勾选了不再询问，如果是返回 false，则这时候应该弹出对话框提示用户去系统设置页面打开权限
        val shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale(permissions[0])
        permissionViewModel.granted.value = hasAllPermissionsGranted(grantResults)
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
    internal fun isGranted(permission: String): Boolean {
        activity?.let {
            return it.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } ?: return false
    }

    @TargetApi(Build.VERSION_CODES.M)
    internal fun isRevoked(permission: String): Boolean {
        activity?.let { activity ->
            activity.packageManager?.let {
                return it.isPermissionRevokedByPolicy(permission, activity.packageName)
            } ?: return false
        } ?: return false
    }
}