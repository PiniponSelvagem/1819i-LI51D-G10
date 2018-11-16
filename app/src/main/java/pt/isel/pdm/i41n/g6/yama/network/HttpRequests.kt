package pt.isel.pdm.i41n.g6.yama.network

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.StringRequest
import java.lang.Exception


object HttpRequests {
    val TAG: String = HttpRequests::class.java.simpleName;

    private lateinit var queue: RequestQueue

    public fun init(context: Context) {
        // Instantiate the RequestQueue.
        Log.i(TAG, "Init called")
        queue = Volley.newRequestQueue(context)
    }

    private fun requestString(
            method: Int,
            url: String,
            headers: Map<String, String>,
            resp: (String) -> Unit,
            err: (Exception) -> Unit
    ) {
        val req = HeadersStringRequest(
                Request.Method.GET, url, headers,
                onResponse(resp),
                onError(err)
        )
        queue.add(req)
    }

    private fun requestBitmap(
            url: String,
            headers: Map<String, String>,
            maxWidth: Int,
            maxHeight: Int,
            resp: (Bitmap) -> Unit,
            err: (Exception) -> Unit
    ) {
        val req = HeadersImageRequest(
                url, headers,
                maxWidth, maxHeight,
                onResponse(resp),
                onError(err)
        )
        queue.add(req)
    }

    fun getString(url: String, headers: Map<String, String> = mapOf(),
                  resp: (String) -> Unit, err: (Exception) -> Unit) {
        requestString(Request.Method.GET, url, headers, resp, err)
    }

    fun getBitmap(url: String, maxWidth: Int, maxHeight: Int, headers: Map<String, String> = mapOf(),
                  resp: (Bitmap) -> Unit, err: (Exception) -> Unit) {
        requestBitmap(url, headers, maxWidth, maxHeight, resp, err)
    }

    private fun <T> onResponse(resp: (T) -> Unit): Response.Listener<T> = Response.Listener {
        str -> resp(str)
    }

    //TODO: WILL CRASH APP, to implement
    private fun onError(err: (Exception) -> Unit): Response.ErrorListener = Response.ErrorListener {
        e -> err(e)
    }





    class HeadersStringRequest(method: Int,
                               url: String,
                               val hdrs: Map<String, String>,
                               listener: Response.Listener<String>,
                               errorListener: Response.ErrorListener
    ) : StringRequest(method,url, listener, errorListener) {

        override fun getHeaders(): MutableMap<String, String> = hdrs.toMutableMap()

    }

    class HeadersImageRequest(url: String,
                              val hdrs: Map<String, String>,
                              maxWidth: Int,
                              maxHeight: Int,
                              listener: Response.Listener<Bitmap>,
                              errorListener: Response.ErrorListener
    ) : ImageRequest(url, listener, maxWidth, maxHeight, Bitmap.Config.ARGB_8888, errorListener ) {

        override fun getHeaders(): MutableMap<String, String> = hdrs.toMutableMap()

    }

}