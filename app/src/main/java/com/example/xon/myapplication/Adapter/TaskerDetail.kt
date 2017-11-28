package com.example.xon.myapplication.Adapter

import android.os.AsyncTask
import android.util.Log
import com.example.xon.myapplication.Model.DataSongQualitySimple
import com.example.xon.myapplication.Model.Qualities
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.regex.Pattern
import javax.net.ssl.HttpsURLConnection

/**
 * Created by Administrator on 11/24/2017.
 */
class TaskerDetail : AsyncTask<String, Void, ArrayList<DataSongQualitySimple>> {
    constructor() : super()

    override fun onProgressUpdate(vararg values: Void?) {

    }

    fun getSourceJsonWeb(url: String): String {
        var Result = ""
        try {

            val URL = URL(url)

            //val URL = URL("http://search.chiasenhac.vn/search.php?s=Thuong")
            if (url.indexOf("https", 0, false) != -1) {
                val connect = URL.openConnection() as HttpsURLConnection
                connect.readTimeout = 3000
                connect.connectTimeout = 3000
                connect.requestMethod = "GET"
                connect.connect()

                val responseCode: Int = connect.responseCode
                Log.d("Tag", "Response Code " + responseCode)
                if (responseCode == 200) {
                    val stream: InputStream = connect.inputStream
                    Result = ConvertoString(stream)
                }
            } else {
                val connect = URL.openConnection() as HttpURLConnection
                connect.readTimeout = 3000
                connect.connectTimeout = 3000
                connect.requestMethod = "GET"
                connect.addRequestProperty("Connection", "keep-alive")
                connect.addRequestProperty("Content-Type", "text/html");
                connect.addRequestProperty("Vary", "Accept-Encoding");
                connect.addRequestProperty("Vary", "Accept-Encoding");
                connect.addRequestProperty("Vary", "Accept-Encoding");
                connect.addRequestProperty("Vary", "Accept-Encoding");
                connect.addRequestProperty("Cache-Control", "max-age=0");
                connect.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
                connect.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
                //connect.addRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
                connect.addRequestProperty("Accept-Language", "vi-VN,vi;q=0.8,fr-FR;q=0.6,fr;q=0.4,en-US;q=0.2,en;q=0.2");

                connect.connect()

                val responseCode: Int = connect.responseCode
                Log.d("Tag", "Response Code " + responseCode)
                if (responseCode == 200) {
                    val stream: InputStream = connect.inputStream
                    Result = ConvertoString(stream)
                }
            }


        } catch (Ex: Exception) {
            Log.d("", "Exception " + Ex.message)
        }
        return Result
    }

    private fun ConvertoString(stream: InputStream): String {
        var result = ""
        val isReader = InputStreamReader(stream)
        val bReader = BufferedReader(isReader)

        try {

            //while (true) {
            //    temp = bReader.readLine()
            //    if (temp == null)
            //        break
            //    result += temp

            result = bReader.readText()
            bReader.close()
            isReader.close()

        } catch (ex: Exception) {
            Log.e("Tag", "Error checking data" + ex.printStackTrace())
        }
        return result
    }

    override fun doInBackground(vararg p0: String?): ArrayList<DataSongQualitySimple> {

        var urlSearch: String = "http://search.chiasenhac.vn/search.php?s=" + URLEncoder.encode(p0[0], "UTF-8")
        var sourceSearch = getSourceJsonWeb(urlSearch)

        var m = Pattern.compile("tenbh(\\w|\\W)+?f=\"((\\w|\\W)+?)\"(\\w|\\W)+?>((\\w|\\W)+?)<(\\w|\\W)+?<p>((\\w|\\W)+?)<(\\w|\\W)+?color(\\w|\\W)+?>((\\w|\\W)+?)<").matcher(sourceSearch)
        var countSearchFinded = 0
        var tempSongs: ArrayList<DataSongQualitySimple> = arrayListOf()

        while (m.find() && countSearchFinded < 4) {
            if (m.group(2).indexOf("video") != -1) {
                continue
            }

            var song = DataSongQualitySimple(m.group(5),
                    m.group(8), coverToQualities(m.group(12)), m.group(2).replace(".html", "_download.html"))

            tempSongs.add(song)

            countSearchFinded += 1
        }
        return tempSongs
    }

    override fun onPostExecute(result: ArrayList<DataSongQualitySimple>?) {
        super.onPostExecute(result)

    }

    private fun coverToQualities(quality: String): Qualities {
        when (quality) {
            "Lossless" -> return Qualities.Lossless
            "320kbps" -> return Qualities.q320
            "128kbps" -> return Qualities.q128
            "500kbps" -> return Qualities.q500
        }
        return Qualities.q128
    }

    override fun onPreExecute() {
        super.onPreExecute()

    }
}