package com.vegcale.architecture.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

private const val TAG = "MyActivity"
class BitmapHelper {
    @Composable
    fun vectorToBitmap(
        @DrawableRes id: Int
    ): BitmapDescriptor {
        Log.i(TAG, "Start")

        val context = LocalContext.current
        val vectorDrawable = ContextCompat.getDrawable(context, id)
        if (vectorDrawable == null) {
            Log.e(TAG, "End: Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)

        Log.i(TAG, "End")

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}