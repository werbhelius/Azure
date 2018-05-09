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
                    grantedBlock?.let { it(granted)}
                }
            })
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    internal fun requestAllPermissions(vararg permission: String) {
        requestPermissions(permission, PERMISSIONS_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != PERMISSIONS_REQUEST_CODE) return
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

    private fun shouldShowRequestPermissionRationale(permissions: Array<out String>): Boolean{
        for (permission in permissions) {
            if (shouldShowRequestPermissionRationale(permission)) {
                return true
            }
        }
        return false
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