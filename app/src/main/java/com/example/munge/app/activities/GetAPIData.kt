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

class GetAPIData(val prev: String) : AsyncTask<String, String, JSONArray>() {

    val CONNECTON_TIMEOUT_MILLISECONDS = 60000

    override fun onPreExecute() {
        // Before doInBackground
    }

    lateinit var journeys : JSONArray
    private val obj = JSONArray()

    override fun doInBackground(vararg urls: String?): JSONArray? {
        var urlConnection: HttpURLConnection? = null

        try {
            val url = URL(urls[0])

            Log.d("data", url.toString())

            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connectTimeout = CONNECTON_TIMEOUT_MILLISECONDS
            urlConnection.readTimeout = CONNECTON_TIMEOUT_MILLISECONDS

            val inString = streamToString(urlConnection.inputStream)

            if (prev == "destination") {
                val convertDataToJson = xml2json(inString).convert()

//                Log.d("journeys", convertDataToJson.toString())

//                start = convertDataToJson.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("GetStartEndPointResponse").getJSONObject("GetStartEndPointResult").getJSONObject("StartPoints").getJSONArray("Point")
//                end = convertDataToJson.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("GetStartEndPointResponse").getJSONObject("GetStartEndPointResult").getJSONObject("EndPoints").getJSONArray("Point")
//
//
//                try {
//                    val list1 = JSONObject()
//                    list1.put("start", start)
//                    list1.put("end", end)
//                    obj.put(list1)
//                } catch (e1: JSONException) {
//                    e1.printStackTrace()
//                }
//
                journeys = convertDataToJson.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("GetJourneyResponse").getJSONObject("GetJourneyResult").getJSONObject("Journeys").getJSONArray("Journey")
                //.getJSONObject("Journeys")


                Log.d("journeys", journeys::class.toString())
                Log.d("journeys", journeys.toString())
                Log.d("journeys", journeys.length().toString())
//                Log.d("journeys", journeys.getJSONObject("Journeys").toString())
//                Log.d("journeys", journeys.getJSONObject("Journeys").length().toString())
//                Log.d("journeys", journeys.getJSONObject("Journeys").getJSONArray("Journey").toString())
//                Log.d("journeys", journeys.getJSONObject("Journeys").getJSONArray("Journey").length().toString())
//
//                try {
//                    val list1 = JSONObject()
//                    list1.put("journeys", journeys)
//                    obj.put(list1)
//                } catch (e1: JSONException) {
//                    e1.printStackTrace()
//                    Log.d("journeys", e1.toString())
//                }

                return journeys
            } else if (prev == "journey") {
                obj.put(inString)

                try {
                    obj.put(inString)
                } catch (e1: JSONException) {
                    e1.printStackTrace()
                }

                return obj
            }
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