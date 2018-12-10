package com.example.munge.app.activities

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader



fun streamToString(inputStream: InputStream): String {

    val bufferReader = BufferedReader(InputStreamReader(inputStream))
    var line: String
    var result = ""

    try {
        do {
            line = bufferReader.readLine()
            if (line != null) {
                result += line
            }
        } while (line != null)
        inputStream.close()
    } catch (ex: Exception) {

    }

    return result
}