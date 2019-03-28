package com.werb.azure

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by wanbo on 2018/4/9.
 */
internal class PermissionViewModel : ViewModel() {

    /** means all permission was granted */
    val granted = MutableLiveData<Boolean>()

}