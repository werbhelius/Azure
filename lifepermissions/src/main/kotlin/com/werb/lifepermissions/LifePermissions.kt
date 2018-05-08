package com.werb.lifepermissions

import android.os.Build
import android.support.v4.app.FragmentActivity

/**
 * Created by wanbo on 2018/4/9.
 */
class LifePermissions(private val activity: FragmentActivity) {

    private val tag = "LivePermissions"
    private val livePermissionFragment: LifePermissionFragment by lazy { getLivePermissionFragment(activity) }
    private lateinit var permissions: Array<out String>

    fun checkPermission(vararg permissions: String): Boolean = livePermissionFragment.checkPermissionsGranted(*permissions)

    fun permissions(vararg permissions: String): LifePermissions {
        this.permissions = permissions
        return this
    }

    fun subscribe(grantedBlock: (isGranted: Boolean) -> Unit): LifePermissions {
        livePermissionFragment.grantedBlock = grantedBlock
        return this
    }

    fun request() {
        if (!checkPermission(*permissions)) {
            livePermissionFragment.requestAllPermissions(*permissions)
        } else {
            livePermissionFragment.grantedBlock?.let { it(true) }
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

}