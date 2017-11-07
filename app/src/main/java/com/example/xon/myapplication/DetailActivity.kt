package com.example.xon.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.R.attr.data
import android.R.attr.data
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import com.example.xon.myapplication.Model.*
import org.json.JSONObject


class DetailActivity : AppCompatActivity() {

    var urlWeb : String =""

    lateinit var rec : RecyclerView
    lateinit var recadapter : RecycleViewAdapter
    var data: ArrayList<DetailModel> = ArrayList<DetailModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        urlWeb = intent.getStringExtra("URLWeb")
        //Lay source web
        var source = getSourceJsonWeb("https://mp3.zing.vn/xhr/media/get-source?type=album&key=LHcGtkHumXznGDByGtFmLHTZpHigxBsGZ")
        //Tao class de chuyen qua json

        //Doi tuong JSONObject goc mieu ta toan bo Json
        var jsonRoot : JSONObject = JSONObject(source)

        //Chuyen data json vao dataWeb
        var dataO =  jsonRoot.getJSONObject("data")
        var jsonArrayItem =  dataO.getJSONArray("items")

        //Tao cai data Web
        var dataWeb = DataWeb()
        dataWeb.isVip = dataO.getBoolean("is_vip")


        for (i in 0..jsonArrayItem.length()-1) {
            var info = InfoDetail()
            var jsonItem = jsonArrayItem.getJSONObject(i)

            //Dua vao InfoDetail
            info.Id = jsonItem.optString("id")
            info.Name = jsonItem.optString("name")
            info.Title = jsonItem.optString("title")
            info.Code = jsonItem.optString("code")
            var artistsArray = jsonItem.optJSONArray("artists")

            for (j in 0..artistsArray.length()-1){
                var artist = Artist()
                var jsonArtist = artistsArray.getJSONObject(j)
                artist.Name = jsonArtist.optString("name")
                artist.Link= jsonArtist.optString("link")
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

            var quality = Quality()
            quality.Q128 = jsonItem.optJSONObject("source").optString("128")
            quality.Q320 = jsonItem.optJSONObject("source").optString("320")
            info.Sources = quality

            var artistInfo = ArtistInfo()
            artistInfo.Id = jsonItem.optJSONObject("artist").optString("id")
            artistInfo.Name = jsonItem.optJSONObject("artist").optString("name")
            artistInfo.Link = jsonItem.optJSONObject("artist").optString("link")
            artistInfo.Cover = jsonItem.optJSONObject("artist").optString("cover")
            artistInfo.Thumbnail = jsonItem.optJSONObject("artist").optString("thumbnail")

            info.ArtistInf = artistInfo

            //Chuyen vao dataweb
            dataWeb.InfoDeltails.add(info)


        }

        rec = findViewById<RecyclerView>(R.id.recid)

        for (i in 0..dataWeb.InfoDeltails.size-1)
        {
            data.add(DetailModel(dataWeb.InfoDeltails[i].Title,dataWeb.InfoDeltails[i].ArtistsName,dataWeb.InfoDeltails[i].Link,dataWeb.InfoDeltails[i].Lyric,dataWeb.InfoDeltails[i].STT))
        }
        recadapter = RecycleViewAdapter(data)

        var layoutManager : LinearLayoutManager =LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rec.layoutManager = layoutManager
        rec.adapter = recadapter
    }

    fun getSourceJsonWeb(url:String) : String{
        return TaskerExample().execute(url).get()
    }
}
