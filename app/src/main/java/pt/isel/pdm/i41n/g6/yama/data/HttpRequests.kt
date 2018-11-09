package pt.isel.pdm.i41n.g6.yama.data

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

object HttpRequests {

    private lateinit var queue: RequestQueue

    public fun init(context: Context) {
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(context)
    }

    private fun request(method: Int, url: String, cb: (String) -> Unit) {

        val req = StringRequest(
                Request.Method.GET, url,
                onResponse(cb),
                onError()
        )
        queue.add(req)
    }


    fun get(url: String, cb: (String) -> Unit) {
        request(Request.Method.GET, url, cb)
    }

    fun delete(url: String, cb: (String) -> Unit) {
        val req: StringRequest = StringRequest(
                Request.Method.GET, url,
                onResponse(cb),
                onError()
        )
        HttpRequests.queue.add(req)
    }

    private fun onResponse(cb: (String) -> Unit): Response.Listener<String> = Response.Listener { str -> cb(str) }

    private fun onError(): Response.ErrorListener = Response.ErrorListener { err -> throw Exception(err.message) }

}