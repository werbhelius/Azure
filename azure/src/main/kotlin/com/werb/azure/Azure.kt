package com.werb.azure

import androidx.fragment.app.FragmentActivity

/**
 * Created by wanbo on 2018/4/9.
 */
class Azure(private val activity: FragmentActivity) {

    private val tag = "LivePermissions"
    private val livePermissionFragment: AzureFragment by lazy { getLivePermissionFragment(activity) }
    private lateinit var permissions: Array<out String>

    fun checkPermission(vararg permissions: String): Boolean = livePermissionFragment.checkPermissionsGranted(*permissions)

    fun permissions(vararg permissions: String): Azure {
        this.permissions = permissions
        return this
    }

    fun subscribe(grantedBlock: (isGranted: Boolean) -> Unit): Azure {
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

    private fun getLivePermissionFragment(activity: FragmentActivity): AzureFragment {
        var livePermissionFragment: AzureFragment? = findLivePermissionFragment(activity)
        if (livePermissionFragment == null) {
            livePermissionFragment = AzureFragment()
            val fragmentManager = activity.supportFragmentManager
            fragmentManager
                .beginTransaction()
                .add(livePermissionFragment, tag)
                .commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }
        return livePermissionFragment
    }

    private fun findLivePermissionFragment(activity: FragmentActivity): AzureFragment? {
        return activity.supportFragmentManager.findFragmentByTag(tag) as AzureFragment?
    }

}