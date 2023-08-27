package com.vegcale.architecture.util

import android.content.Context
import android.content.pm.PackageManager

fun isPermissionGranted(context: Context, permission: String): Boolean {
    val permissionState = context.checkSelfPermission(permission)

    return permissionState == PackageManager.PERMISSION_GRANTED
}