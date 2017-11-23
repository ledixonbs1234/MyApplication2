package com.example.xon.myapplication.View

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xon.myapplication.Adapter.RecycleViewAdapter
import com.example.xon.myapplication.Model.DataSongFullSimple
import com.example.xon.myapplication.Model.DataSongManager
import com.example.xon.myapplication.Model.DataSongQualitySimple
import com.example.xon.myapplication.Model.Quality
import com.example.xon.myapplication.R
import org.json.JSONObject
import java.net.URLEncoder
import java.util.regex.Pattern

/**
 * Created by XON on 11/9/2017.
 */
class FragmentDetail : Fragment {
    lateinit var viewRoot: View
    lateinit var rec: RecyclerView
    lateinit var recadapter: RecycleViewAdapter
    lateinit var contextRoot: Context
    lateinit var mSongs: ArrayList<DataSongManager>

    constructor()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewRoot = inflater.inflate(R.layout.fragment_detail, container, false)

        //Khoi tao
        initView()

        //dua vao id list lay du lieu tu chiasenhac
        for (song in mSongs) {
            song.tempSongs = searchListSongByNameFromChiaSeNhac(song.id)
        }
        rec = viewRoot.findViewById<RecyclerView>(R.id.recid)

        recadapter = RecycleViewAdapter(mSongs, contextRoot)

        val layoutManager = LinearLayoutManager(this.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rec.layoutManager = layoutManager
        rec.adapter = recadapter
        return viewRoot
    }

    private fun searchListSongByNameFromChiaSeNhac(songName: String): ArrayList<DataSongQualitySimple> {
        //Lấy địa chỉ của search nhạc
        //Lấy source của địa chỉ
        //Thưc hiện lọc đuọc 2 url ,5 tên bài hát , 8 ca sĩ

        // và tìm ra 4 cái kết quả

        var urlSearch: String = "http://search.chiasenhac.vn/search.php?s=" + URLEncoder.encode(songName, "UTF-8")
        var temp = "http://search.chiasenhac.vn/search.php?s=Thay+La+Yeu+Thuong"
        var sourceSearch = getSourceJsonWeb(urlSearch)

        var m = Pattern.compile("tenbh(\\w|\\W)+?href=\"((\\w|\\W)+?)\"(\\w|\\W)+?>((\\w|\\W)+?)<(\\w|\\W)+?<p>((\\w|\\W)+?)<").matcher(sourceSearch)
        var countSearchFinded = 0
        var tempSongs: ArrayList<DataSongQualitySimple> = arrayListOf()

        while (m.find() && countSearchFinded < 4) {
            if (m.group(2).indexOf("video") != -1) {
                continue
            }
            // Thay đổi link để lấy link Download
            var sourceDownload = getSourceJsonWeb(m.group(2).replace(".html", "_download.html"))

            var quality: Quality = Quality()

            var nFill = Pattern.compile("downloadlink2(\\W|\\w)+?center").matcher(sourceDownload)
            if (nFill.find()) {
                var n = Pattern.compile("href=\"((\\W|\\w)+?)\"").matcher(nFill.group(0))
                //Link dang
                //http://chiasenhac.vn/download2.php?v1=1843&v2=1&v3=1YJ2AUA-2XMG2ebG&v4=128&v5=Da%20Lo%20Yeu%20Em%20Nhieu%20-%20JustaTee [128kbps_MP3].mp3

                while (n.find()) {

                    when (n.group(1).substring(n.group(1).indexOf('['), n.group(1).indexOf('[') + 4)) {
                        "[128" -> {
                            quality.q128 = n.group(1)
                        }
                        "[320" -> {
                            quality.q320 = n.group(1)
                        }
                        "[500" -> {
                            quality.q500 = n.group(1)
                        }
                        "[Los" -> {
                            quality.lossless = n.group(1)
                        }
                    }
                }

            }

            var song = DataSongQualitySimple(m.group(5),
                    m.group(8),
                    quality)

            tempSongs.add(song)

            countSearchFinded += 1
        }
        return tempSongs
    }

    private fun initView() {
        val urlWeb = arguments!!.getString("URL")

        //Lay token
        val sourceWeb = getSourceJsonWeb(urlWeb)

        var tokenPattern = Pattern.compile("id=\"zplayerjs-wrapper\"(\\w|\\W)+?key=((\\w|\\W)+?)\"").matcher(sourceWeb)

        var token: String = ""
        if (tokenPattern.find()) {
            token = tokenPattern.group(2)
        }

        //Lay json
        val source = getSourceJsonWeb("https://mp3.zing.vn/xhr/media/get-source?type=album&key=" + token)

        //Doi tuong JSONObject goc mieu ta toan bo Json
        val jsonRoot: JSONObject = JSONObject(source)

        //Chuyen data json vao tao thanh List
        val dataO = jsonRoot.getJSONObject("data")
        val jsonArrayItem = dataO.getJSONArray("items")

        //Tạo list
        mSongs = arrayListOf()

        for (i in 0..jsonArrayItem.length() - 1) {

            var jsonItem = jsonArrayItem.getJSONObject(i)

            //Lấy chất lượng
            var quality = Quality()

            //Vi khong co https nen them vao thoi
            quality.q128 = "https:" + jsonItem.optJSONObject("source").optString("128")
            quality.q320 = jsonItem.optJSONObject("source").optString("320")

            var data = DataSongFullSimple(
                    jsonItem.optString("name"),
                    jsonItem.optString("artists_names"),
                    jsonItem.optString("link"),
                    jsonItem.optString("thumbnail"),
                    quality
            )
            mSongs.add(DataSongManager(data.songName, data, null))
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.contextRoot = context!!

    }


    fun getSourceJsonWeb(url: String): String {
        return TaskerExample().execute(url).get()
    }
}