package pt.isel.pdm.i41n.g6.yama.data.httprequests

import android.content.Context
import com.android.volley.*
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.StringRequest


object Volley {

    private lateinit var queue: RequestQueue

    fun init(context: Context) {
        queue = Volley.newRequestQueue(context)
    }

    fun getRequestQueue(): RequestQueue {
        return queue
    }

    class HeadersStringRequest(method: Int,
                               url: String,
                               val hdrs: Map<String, String>,
                               listener: Response.Listener<String>,
                               errorListener: Response.ErrorListener
    ) : StringRequest(method,url, listener, errorListener ) {

        override fun getHeaders(): MutableMap<String, String> = hdrs.toMutableMap()

    }
}



    /*

    private fun request(
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


    fun get(url: String, headers: Map<String, String> = mapOf(), cb: (String) -> Unit) {
        request(Request.Method.GET, url, headers, cb)
    }

    private fun onResponse(cb: (String) -> Unit): Response.Listener<String> = Response.Listener {
        str -> cb(str)
    }

    private fun onError(): Response.ErrorListener = Response.ErrorListener {
        err -> throw Exception(err)
    }
    /*
        err -> run {
            if (err is AuthFailureError) {
                println(err.printStackTrace())
            }
        }
    }
    */


    class HeadersStringRequest(method: Int,
                               url: String,
                               val hdrs: Map<String, String>,
                               listener: Response.Listener<String>,
                               errorListener: Response.ErrorListener
    ) : StringRequest(method,url, listener, errorListener ) {

        override fun getHeaders(): MutableMap<String, String> = hdrs.toMutableMap()

    }
    */