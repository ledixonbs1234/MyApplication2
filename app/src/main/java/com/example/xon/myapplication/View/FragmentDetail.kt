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
import com.example.xon.myapplication.Model.Quality
import com.example.xon.myapplication.R
import org.json.JSONObject
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


        rec = viewRoot.findViewById<RecyclerView>(R.id.recid)

        recadapter = RecycleViewAdapter(mSongs, contextRoot)

        val layoutManager = LinearLayoutManager(this.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rec.layoutManager = layoutManager
        rec.adapter = recadapter
        // vao id list lay du lieu tu chiasenhac
        //for (song in mSongs) {
        //    song.tempSongs = TaskerDetail().execute(song.id).get()
        //   rec.adapter.notifyDataSetChanged()
        //}

        return viewRoot
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

            var data = DataSongFullSimple(
                    jsonItem.optString("name"),
                    jsonItem.optString("artists_names"),
                    "https://mp3.zing.vn" + jsonItem.optString("link"),
                    null,
                    jsonItem.optString("thumbnail"),
                    quality
            )
            mSongs.add(DataSongManager(data.songName, data, null, null))
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