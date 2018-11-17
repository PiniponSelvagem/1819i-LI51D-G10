package pt.isel.pdm.li51d.g10.yama.utils

import android.content.Context
import android.widget.Toast
import com.android.volley.*

fun showHttpErrorToast(context: Context, e: Exception) {
    if (e is TimeoutError || e is NoConnectionError) {
        Toast.makeText(context, "Timeout", Toast.LENGTH_LONG).show()
    } else if (e is AuthFailureError) {
        Toast.makeText(context, "Authentication Error", Toast.LENGTH_LONG).show()
    } else if (e is ServerError) {
        Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show()
    } else if (e is NetworkError) {
        Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show()
    } else if (e is ParseError) {
        Toast.makeText(context, "Parse Error", Toast.LENGTH_LONG).show()
    }
}