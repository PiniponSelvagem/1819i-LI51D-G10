package pt.isel.pdm.i41n.g6.yama.network

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.StringRequest


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
            cb: (String) -> Unit
    ) {
        val req = HeadersStringRequest(
                Request.Method.GET, url, headers,
                onResponse(cb),
                onError()
        )
        queue.add(req)
    }

    private fun requestBitmap(
            url: String,
            headers: Map<String, String>,
            maxWidth: Int,
            maxHeight: Int,
            cb: (Bitmap) -> Unit
    ) {
        val req = HeadersImageRequest(
                url, headers,
                maxWidth, maxHeight,
                onResponse(cb),
                onError()
        )
        queue.add(req)
    }

    fun getString(url: String, headers: Map<String, String> = mapOf(), cb: (String) -> Unit) {
        requestString(Request.Method.GET, url, headers, cb)
    }

    fun getBitmap(url: String, maxWidth: Int, maxHeight: Int, headers: Map<String, String> = mapOf(), cb: (Bitmap) -> Unit) {
        requestBitmap(url, headers, maxWidth, maxHeight, cb)
    }

    private fun <T> onResponse(cb: (T) -> Unit): Response.Listener<T> = Response.Listener { str -> cb(str) }

    //TODO: WILL CRASH APP, to implement
    private fun onError(): Response.ErrorListener = Response.ErrorListener {
        TODO("NOT IMPLEMENTED")
        //err -> throw Exception(err.message)
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