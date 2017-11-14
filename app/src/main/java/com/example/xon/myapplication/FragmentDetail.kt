package com.example.xon.myapplication

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xon.myapplication.Model.*
import org.json.JSONObject

/**
 * Created by XON on 11/9/2017.
 */
class FragmentDetail : Fragment {
    lateinit var viewRoot: View
    lateinit var rec: RecyclerView
    lateinit var recadapter: RecycleViewAdapter
    var data: ArrayList<DetailModel> = ArrayList<DetailModel>()
    lateinit var contextRoot: Context

    constructor()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewRoot = inflater.inflate(R.layout.fragment_detail, container, false)

        initView()
        return viewRoot
    }

    private fun initView() {
        val urlWeb = arguments!!.getString("URL")
        //Lay source web
        val source = getSourceJsonWeb("https://mp3.zing.vn/xhr/media/get-source?type=album&key=LHcGtkHumXznGDByGtFmLHTZpHigxBsGZ")
        //Tao class de chuyen qua json

        //Doi tuong JSONObject goc mieu ta toan bo Json
        val jsonRoot: JSONObject = JSONObject(source)

        //Chuyen data json vao dataWeb
        val dataO = jsonRoot.getJSONObject("data")
        val jsonArrayItem = dataO.getJSONArray("items")

        //Tao cai data Web
        var dataWeb = DataWeb()
        dataWeb.isVip = dataO.getBoolean("is_vip")


        for (i in 0..jsonArrayItem.length() - 1) {
            val info = InfoDetail()
            var jsonItem = jsonArrayItem.getJSONObject(i)

            //Dua vao InfoDetail
            info.Id = jsonItem.optString("id")
            info.Name = jsonItem.optString("name")
            info.Title = jsonItem.optString("title")
            info.Code = jsonItem.optString("code")
            val artistsArray = jsonItem.optJSONArray("artists")

            for (j in 0..artistsArray.length() - 1) {
                var artist = Artist()
                val jsonArtist = artistsArray.getJSONObject(j)
                artist.Name = jsonArtist.optString("name")
                artist.Link = jsonArtist.optString("link")
                info.Artists.add(artist)
            }

            info.ArtistsName = jsonItem.optString("artists_names")
            info.Performer = jsonItem.optString("performer")
            info.Type = jsonItem.optString("type")
            info.Link = jsonItem.optString("link")
            info.Lyric = jsonItem.optString("lyric")
            info.Thumbnail = jsonItem.optString("thumbnail")
            info.MVLink = jsonItem.optString("mv_link")
            info.STT = jsonItem.optString("order")

            val quality = Quality()

            //Vi khong co https nen them vao thoi
            quality.Q128 = "https:" + jsonItem.optJSONObject("source").optString("128")
            quality.Q320 = jsonItem.optJSONObject("source").optString("320")
            info.Sources = quality

            val artistInfo = ArtistInfo()
            artistInfo.Id = jsonItem.optJSONObject("artist").optString("id")
            artistInfo.Name = jsonItem.optJSONObject("artist").optString("name")
            artistInfo.Link = jsonItem.optJSONObject("artist").optString("link")
            artistInfo.Cover = jsonItem.optJSONObject("artist").optString("cover")
            artistInfo.Thumbnail = jsonItem.optJSONObject("artist").optString("thumbnail")

            info.ArtistInf = artistInfo

            //Chuyen vao dataweb
            dataWeb.InfoDeltails.add(info)


        }

        rec = viewRoot.findViewById<RecyclerView>(R.id.recid)

        for (i in 0..dataWeb.InfoDeltails.size - 1) {
            data.add(DetailModel(dataWeb.InfoDeltails[i].Title
                    , dataWeb.InfoDeltails[i].ArtistsName
                    , dataWeb.InfoDeltails[i].Link
                    , dataWeb.InfoDeltails[i].Lyric
                    , dataWeb.InfoDeltails[i].STT
                    , dataWeb.InfoDeltails[i].Sources))
        }
        recadapter = RecycleViewAdapter(data, contextRoot)

        val layoutManager = LinearLayoutManager(this.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rec.layoutManager = layoutManager
        rec.adapter = recadapter
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.contextRoot = context!!

    }


    fun getSourceJsonWeb(url: String): String {
        return TaskerExample().execute(url).get()
    }
}