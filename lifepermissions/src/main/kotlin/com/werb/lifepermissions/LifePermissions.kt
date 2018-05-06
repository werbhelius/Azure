package com.werb.lifepermissions

import android.os.Build
import android.support.v4.app.FragmentActivity

/**
 * Created by wanbo on 2018/4/9.
 */
class LifePermissions(private val activity: FragmentActivity) {

    private val tag = "LivePermissions"
    private val livePermissionFragment: LifePermissionFragment by lazy { getLivePermissionFragment(activity) }
    private lateinit var block: (granted: Boolean) -> Unit
    private lateinit var permissions: Array<out String>

    fun check(vararg permissions: String): Boolean {
        var hasPermission = true
        permissions.forEach {
            hasPermission = isGranted(it)
        }
        return hasPermission
    }

    fun permissions(vararg permissions: String): LifePermissions {
        this.permissions = permissions
        return this
    }

    fun subscribe(block: (granted: Boolean) -> Unit): LifePermissions {
        this.block = block
        return this
    }

    fun request(){
        execute()
    }

    private fun execute() {
        val granted = check(*permissions)
        if (!granted) {
            livePermissionFragment.requestAllPermissions(*permissions, block = block)
        }
    }

    private fun getLivePermissionFragment(activity: FragmentActivity): LifePermissionFragment {
        var livePermissionFragment: LifePermissionFragment? = findLivePermissionFragment(activity)
        if (livePermissionFragment == null) {
            livePermissionFragment = LifePermissionFragment()
            val fragmentManager = activity.supportFragmentManager
            fragmentManager
                .beginTransaction()
                .add(livePermissionFragment, tag)
                .commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }
        return livePermissionFragment
    }

    private fun findLivePermissionFragment(activity: FragmentActivity): LifePermissionFragment? {
        return activity.supportFragmentManager.findFragmentByTag(tag) as LifePermissionFragment?
    }

    private fun isGranted(permission: String): Boolean {
        return !isMarshmallow() || livePermissionFragment.isGranted(permission)
    }

    private fun isRevoked(permission: String): Boolean {
        return isMarshmallow() && livePermissionFragment.isRevoked(permission)
    }

    private fun isMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

}