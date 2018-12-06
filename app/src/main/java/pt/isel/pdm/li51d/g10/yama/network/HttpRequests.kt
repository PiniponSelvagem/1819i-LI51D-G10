package pt.isel.pdm.li51d.g10.yama.network

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


object HttpRequests {
    val TAG: String = HttpRequests::class.java.simpleName;

    private lateinit var queue: RequestQueue

    fun init(context: Context) {
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

        //info (maybe for later use): https://stackoverflow.com/a/32879935
        req.setShouldCache(false) //dont cache data, which would send old data to the viewModel (LiveData)
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

        /*
        disabled for now, since new images will have different url
        but uncomment line below if a user updates their avatar, and this request "shows" the old image
         */
        //req.setShouldCache(false) //dont cache data, which would send old data to the viewModel (LiveData)

        queue.add(req)
    }

    fun getString(url: String, headers: Map<String, String> = mapOf(),
                  resp: (String) -> Unit, err: (Exception) -> Unit) {
        requestString(Request.Method.GET, url, headers, resp, err)
    }

    fun getBitmap(url: String, maxWidth: Int, maxHeight: Int, headers: Map<String, String>,
                  resp: (Bitmap) -> Unit, err: (Exception) -> Unit) {
        requestBitmap(url, headers, maxWidth, maxHeight, resp, err)
    }

    private fun <T> onResponse(resp: (T) -> Unit): Response.Listener<T> = Response.Listener {
        r -> resp(r)
    }

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