package com.example.munge.app.activities

import android.os.AsyncTask
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import java.net.HttpURLConnection
import java.net.URL


// https://grokonez.com/android/kotlin-http-call-with-asynctask-example-android

class GetAPIData(val prev: String) : AsyncTask<String, String, JSONArray>() {

    val CONNECTON_TIMEOUT_MILLISECONDS = 60000

    override fun onPreExecute() {
        // Before doInBackground
    }

    lateinit var journeys : JSONArray
    lateinit var departures : JSONArray
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

            when (prev) {
                "destination_journey" -> {
                    val convertDataToJson = xml2json(inString).convert()

                    journeys = convertDataToJson.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("GetJourneyResponse").getJSONObject("GetJourneyResult").getJSONObject("Journeys").getJSONArray("Journey")

                    return journeys
                }
                "journey" -> {
                    obj.put(inString)

                    try {
                        obj.put(inString)
                    } catch (e1: JSONException) {
                        e1.printStackTrace()
                    }

                    return obj
                }
                "destination_departure" -> {
                    val convertDataToJson = xml2json(inString).convert()

                    departures = convertDataToJson.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("GetDepartureArrivalResponse").getJSONObject("GetDepartureArrivalResult").getJSONObject("Lines").getJSONArray("Line")

                    return departures
                }
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