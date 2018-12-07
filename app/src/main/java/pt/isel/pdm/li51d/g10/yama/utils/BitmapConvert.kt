package pt.isel.pdm.li51d.g10.yama.utils

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Color
import java.io.ByteArrayOutputStream


// convert from bitmap to byte array
fun convertBitmapToBytes(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(CompressFormat.PNG, 0, stream)
    return stream.toByteArray()
}

// convert from byte array to bitmap
fun convertBytesToBitmap(bytes: ByteArray?): Bitmap {
    if (bytes != null) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
    val image = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    image.eraseColor(Color.RED)
    return image
}