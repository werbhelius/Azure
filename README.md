# Azure

> Easier to use Android runtime permissions with LiveData

[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/Werb/Azure/blob/master/LICENSE)
 [ ![Download](https://api.bintray.com/packages/werbhelius/maven/azure/images/download.svg) ](https://bintray.com/werbhelius/maven/azure/_latestVersion)
 [![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)

## Dependency
 ```gradle
implementation 'com.werb.azure:azure:0.2.0'
implementation 'android.arch.lifecycle:extensions:1.1.1'
```

## Use
```kotlin
// support dynamic permissions and dangerous permissions
Azure(this)
    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
    .subscribe {
        if (it) {
            // do something when permission isGranted
        } else {
            // do something when permission isDenied
        }
    }.request()
```