# Azure

> Easier to use Android runtime permissions with LiveData ！Easier to use Android runtime permissions with LiveData ！

[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/Werb/Azure/blob/master/LICENSE)
 [ ![Download](https://api.bintray.com/packages/werbhelius/maven/azure/images/download.svg) ](https://bintray.com/werbhelius/maven/azure/_latestVersion)
 [![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)

## Dependency
 ```gradle
implementation 'com.werb.azure:azure:0.1.0'
implementation 'android.arch.lifecycle:extensions:1.1.1'
```

## Use
```kotlin
Azure(this)
    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
    .subscribe {
        if (it) {
            // do something when permission isGranted
        } else {
            // do something when permission isDenied
        }
    }.request()
```