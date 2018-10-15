package com.werb.azure

import android.annotation.TargetApi
import android.os.Build
import android.provider.Settings

/**
 * Created by wanbo on 2018/10/15.
 */


@TargetApi(Build.VERSION_CODES.M)
internal fun specialPermissionCheck(permissions:  MutableList<String>): MutableMap<String, Int>? {
    val map = mutableMapOf<String, Int>()
    permissions
        .asSequence()
        .find { it == Settings.ACTION_WIRELESS_SETTINGS }
        ?.apply {
            map[this] = AzureFragment.PERMISSIONS_WIRELESS_SETTINGS
            permissions.remove(this)
        }
    permissions
        .asSequence()
        .find { it == Settings.ACTION_MANAGE_OVERLAY_PERMISSION }
        ?.apply {
            map[this] = AzureFragment.PERMISSIONS_OVERLAY_PERMISSION
            permissions.remove(this)
        }
    return if (map.isEmpty()) null else map
}
