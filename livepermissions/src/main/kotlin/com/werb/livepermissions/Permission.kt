package com.werb.livepermissions

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * Created by wanbo on 2018/4/9.
 */
internal class PermissionViewModel : ViewModel() {

    val granted = MutableLiveData<Boolean>()

}