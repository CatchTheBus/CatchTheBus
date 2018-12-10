package com.example.munge.app.activities

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.net.HttpURLConnection
import java.net.URL

// https://grokonez.com/android/kotlin-http-call-with-asynctask-example-android

class GetAPIData(private val mainActivity: MainActivity) : AsyncTask<String, String, String>() {

    override fun onPreExecute() {
        // Before doInBackground
    }

    override fun doInBackground(vararg urls: String?): String {
        var urlConnection: HttpURLConnection? = null

        try {
            val url = URL(urls[0])

            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connectTimeout = mainActivity.CONNECTON_TIMEOUT_MILLISECONDS
            urlConnection.readTimeout = mainActivity.CONNECTON_TIMEOUT_MILLISECONDS

            var inString = streamToString(urlConnection.inputStream)

            publishProgress(inString)
        } catch (ex: Exception) {

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect()
            }
        }

        return " "
    }

    @SuppressLint("SetTextI18n")
    override fun onProgressUpdate(vararg values: String?) {

        val convertDataToJson = xml2json(values[0]).convert()

        val convertedDataStartLocation = convertDataToJson.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("GetStartEndPointResponse").getJSONObject("GetStartEndPointResult").getJSONObject("StartPoints").getJSONArray("Point")
        val convertedDataEndLocation = convertDataToJson.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("GetStartEndPointResponse").getJSONObject("GetStartEndPointResult").getJSONObject("EndPoints").getJSONArray("Point")

        for (n in 0 until convertedDataStartLocation.length()) {
            val `object` = convertedDataStartLocation.getJSONObject(n)

            val id = `object`["Id"]
            val name = `object`["Name"]
            val x = `object`["X"]
            val y = `object`["Y"]

            mainActivity.bla.text =
                    "Id: " + id + "\n" +
                    "Name: " + name + "\n" +
                    "X: " + x + "\n" +
                    "Y: " + y
        }

        for (n in 0 until convertedDataEndLocation.length()) {
            val `object` = convertedDataEndLocation.getJSONObject(n)

            val id = `object`["Id"]
            val name = `object`["Name"]
            val x = `object`["X"]
            val y = `object`["Y"]

            mainActivity.bla.text =
                    "Id: " + id + "\n" +
                    "Name: " + name + "\n" +
                    "X: " + x + "\n" +
                    "Y: " + y
        }

    }

    override fun onPostExecute(result: String?) {
        // Done
    }
}