package com.example.munge.app.activities

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import android.widget.TextView
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject



// https://grokonez.com/android/kotlin-http-call-with-asynctask-example-android

class GetAPIData(val textField: TextView) : AsyncTask<String, String, JSONArray>() {

    val CONNECTON_TIMEOUT_MILLISECONDS = 60000

    override fun onPreExecute() {
        // Before doInBackground
    }

    lateinit var start : JSONArray
    lateinit var end : JSONArray
    private val obj = JSONArray()

    override fun doInBackground(vararg urls: String?): JSONArray? {
        var urlConnection: HttpURLConnection? = null

        try {
            val url = URL(urls[0])

            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connectTimeout = CONNECTON_TIMEOUT_MILLISECONDS
            urlConnection.readTimeout = CONNECTON_TIMEOUT_MILLISECONDS

            val inString = streamToString(urlConnection.inputStream)

            val convertDataToJson = xml2json(inString).convert()

            start = convertDataToJson.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("GetStartEndPointResponse").getJSONObject("GetStartEndPointResult").getJSONObject("StartPoints").getJSONArray("Point")
            end = convertDataToJson.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("GetStartEndPointResponse").getJSONObject("GetStartEndPointResult").getJSONObject("EndPoints").getJSONArray("Point")


            try {
                val list1 = JSONObject()
                list1.put("start", start)
                list1.put("end", end)
                obj.put(list1)
            } catch (e1: JSONException) {
                // TODO Auto-generated catch block
                e1.printStackTrace()
            }

            return obj
        } catch (ex: Exception) {

        } finally {
            urlConnection?.disconnect()
        }

        return null
    }

    override fun onPostExecute(result: JSONArray) {
        myMethod(obj)
    }

    private fun myMethod(myValue: JSONArray): JSONArray {
        return myValue
    }
}