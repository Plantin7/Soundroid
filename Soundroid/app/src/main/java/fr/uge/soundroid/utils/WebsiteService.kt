package fr.uge.soundroid.utils

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.uge.soundroid.models.Soundtrack
import org.json.JSONArray
import org.json.JSONObject

object WebsiteService {

    var url: String = "http://192.168.42.234/soundtracks"

    lateinit var requestQueue: RequestQueue

    var post: Boolean = false


    fun initQueue(context: Context) {
        requestQueue = Volley.newRequestQueue(context)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun sendSoundtrackInfos(soundtrack: Soundtrack) {
        val method: Int
        val uri: String
        val request: JsonObjectRequest
        if ( post ) {
            method = Request.Method.POST
            uri = url

            request = object : JsonObjectRequest(
                method,
                uri,
                null,
                Response.Listener<JSONObject> { response ->
                    Log.d(WebsiteService::class.java.name, "Success -> $response")
                },
                Response.ErrorListener { response ->
                    Log.d(WebsiteService::class.java.name, "Error -> ${response}")
                }) {
                override fun getParams(): MutableMap<String, String> {
                    val map =  mutableMapOf(
                        "title" to "${soundtrack.title}",
                        "album" to "${soundtrack.album?.name}",
                        "artist" to "${soundtrack.artist?.name}"
                    )
                    Log.d("GUIGUI", map.toString())
                    return map
                }
            }
        } else {
            method = Request.Method.GET
            uri = String.format("$url?title=%1\$s&album=%2\$s&artist=%3\$s", soundtrack.title, soundtrack.album?.name, soundtrack.artist?.name)


            request = JsonObjectRequest(
                method,
                uri,
                null,
                Response.Listener<JSONObject> { response ->
                    Log.d(WebsiteService::class.java.name, "Success -> $response")
                },
                Response.ErrorListener { response ->
                    Log.d(WebsiteService::class.java.name, "Error -> ${response}")
                })
        }



        // Add the request to the RequestQueue.
        requestQueue.add(request)
    }

}