package com.example.xon.myapplication.View


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.xon.myapplication.Adapter.AdapterBanner
import com.example.xon.myapplication.Adapter.RecycleViewBXHAdapter
import com.example.xon.myapplication.Adapter.SingletonPlayMusic
import com.example.xon.myapplication.InterfaceView.OnClickInterface
import com.example.xon.myapplication.Model.DataSongSimple
import com.example.xon.myapplication.Model.DataWebModel
import com.example.xon.myapplication.R
import com.squareup.picasso.Picasso
import me.relex.circleindicator.CircleIndicator
import org.json.JSONObject
import java.util.*
import java.util.regex.Pattern


/**
 * A simple [Fragment] subclass.
 */
class OnlineFragment : Fragment(), View.OnClickListener {


    //Tao list
    var listImage: ArrayList<DataWebModel> = ArrayList()
    lateinit var viewPager: ViewPager
    lateinit var mViewRoot: View
    lateinit var mContextAc: Context
    lateinit var sourceWeb: String
    lateinit var listener: OnClickInterface
    lateinit var playlists: ArrayList<DataWebModel>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mViewRoot = inflater.inflate(R.layout.fragment_online, container, false)

        return mViewRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // listImage.add(R.drawable.d1)
        // listImage.add(R.drawable.d2)
        // listImage.add(R.drawable.d4)
        // listImage.add(R.drawable.d3)

        val source = "https://mp3.zing.vn"

        mViewRoot.findViewById<Button>(R.id.bxhaumy).setOnClickListener(this)
        mViewRoot.findViewById<Button>(R.id.bxhhanquoc).setOnClickListener(this)
        mViewRoot.findViewById<Button>(R.id.top100).setOnClickListener(this)
        mViewRoot.findViewById<Button>(R.id.realtime).setOnClickListener(this)
        mViewRoot.findViewById<ImageButton>(R.id.chudevatheloai).setOnClickListener(this)
        mViewRoot.findViewById<ImageButton>(R.id.bxhvietnam).setOnClickListener(this)

        var singleton = SingletonPlayMusic.instance
        sourceWeb = TaskerExample().execute(source).get()
        initBanner(sourceWeb)
        initPlaylist(sourceWeb)
        initBangXepHang(sourceWeb)


    }

    override fun onClick(p0: View?) {
        var source = ""
        var linkTrust = ""
        when (p0!!.id) {
            R.id.first -> {
                linkTrust = playlists[0].urlWeb
            }
            R.id.second -> {
                linkTrust = playlists[1].urlWeb
            }
            R.id.third -> {
                linkTrust = playlists[2].urlWeb
            }
            R.id.four -> {
                linkTrust = playlists[3].urlWeb
            }
            R.id.bxhaumy -> {
                source = getSourceJsonWeb("https://mp3.zing.vn/bang-xep-hang/bai-hat-Au-My/IWZ9Z0BW.html")
            }
            R.id.bxhhanquoc -> {
                source = getSourceJsonWeb("https://mp3.zing.vn/bang-xep-hang/bai-hat-Han-Quoc/IWZ9Z0BO.html")
            }
            R.id.realtime -> {
                source = getSourceJsonWeb("https://mp3.zing.vn/album/REAL-TIME-ZING-CHART-Various-Artists/ZO68OC68.html?st=50")
            }
            R.id.top100 -> {
                listener.openTypeDetailFragment("https://mp3.zing.vn/chu-de/Top-100-Hay-Nhat/IWZ9ZI68.html")
            }
            R.id.bxhvietnam -> {
                source = getSourceJsonWeb("https://mp3.zing.vn/bang-xep-hang/bai-hat-Viet-Nam/IWZ9Z08I.html")
            }
            R.id.chudevatheloai -> {
                listener.openTypeFragment()
            }
        }

        if (p0.id == R.id.bxhvietnam || p0.id == R.id.bxhaumy || p0.id == R.id.bxhhanquoc || p0.id == R.id.realtime) {
            var n = Pattern.compile("social -->(\\W|\\w)+?\"((\\W|\\w)+?)\"").matcher(source)
            if (n.find()) {
                linkTrust = "https://mp3.zing.vn" + n.group(2)
            }
            //chuyen link vao listener
        }

        if (p0.id != R.id.top100 && p0.id != R.id.chudevatheloai) {
            //Lấy source xong thì chuyển trang
            listener.openDetailOrPlayerFragment(linkTrust, null)
        }


    }


    fun getSourceJsonWeb(url: String): String {
        return TaskerExample().execute(url).get()
    }

    private fun initBangXepHang(sourceWeb: String?) {
        //Lam 1 cai danh sach
        //Lay source web
        val source = getSourceJsonWeb("https://mp3.zing.vn/xhr/chart-realtime?chart=song&time=-1&count=5")
        //Tao class de chuyen qua json

        //Doi tuong JSONObject goc mieu ta toan bo Json
        val jsonRoot: JSONObject = JSONObject(source)

        //Chuyen data json vao dataWeb
        val dataO = jsonRoot.getJSONObject("data")
        val jsonArrayItem = dataO.getJSONArray("song")

        //Tao cai data Web
        var datas = ArrayList<DataSongSimple>()


        for (i in 0..jsonArrayItem.length() - 1) {

            var jsonItem = jsonArrayItem.getJSONObject(i)

            var data = DataSongSimple(
                    jsonItem.optString("name")
                    , jsonItem.optString("artists_names")
                    , "https://mp3.zing.vn" + jsonItem.optString("link"))

            datas.add(data)
        }

        var rec = mViewRoot.findViewById<RecyclerView>(R.id.bangxephang)

        var recadapter = RecycleViewBXHAdapter(datas, mContextAc)

        val layoutManager = LinearLayoutManager(this.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rec.layoutManager = layoutManager
        rec.adapter = recadapter
    }

    private fun initPlaylist(sourceWeb: String?) {
        //Khi lay duoc ma nguon xong thi phai loc ra bang regex
        val p = Pattern.compile("row fn-list(\\w|\\W)+?mt20")
        val m = p.matcher(sourceWeb)
        var sourceFillPlaylist = ""
        //chay regex 1 lan
        if (m.find())
            sourceFillPlaylist = m.group(0)

        //Rút gọn rồi lọc ra danh sách gồm 8 item
        playlists = ArrayList<DataWebModel>()

        val n = Pattern.compile("href=\"((\\W|\\w)+?)\"(\\W|\\w)+?src=\"((\\W|\\w)+?)\" alt=\"((\\W|\\w)+?)\"").matcher(sourceFillPlaylist)
        while (n.find()) {
            playlists.add(DataWebModel(n.group(4), "https://mp3.zing.vn" + n.group(1), n.group(6)))
        }

        //Lấy ra 3 item để show
        var imageButton = mViewRoot.findViewById<ImageButton>(R.id.first)
        imageButton.setOnClickListener(this)
        Picasso.with(mViewRoot.context).load(playlists[0].urlImage).into(imageButton)
        mViewRoot.findViewById<TextView>(R.id.firsttext).text = playlists[0].title

        var imageButton2 = mViewRoot.findViewById<ImageButton>(R.id.second)
        imageButton2.setOnClickListener(this)
        Picasso.with(mViewRoot.context).load(playlists[1].urlImage).into(imageButton2)
        mViewRoot.findViewById<TextView>(R.id.secondtext).text = playlists[1].title

        var imageButton3 = mViewRoot.findViewById<ImageButton>(R.id.third)
        imageButton3.setOnClickListener(this)
        Picasso.with(mViewRoot.context).load(playlists[2].urlImage).into(imageButton3)
        mViewRoot.findViewById<TextView>(R.id.thirdtext).text = playlists[2].title

        var imageButton4 = mViewRoot.findViewById<ImageButton>(R.id.four)
        imageButton4.setOnClickListener(this)
        Picasso.with(mViewRoot.context).load(playlists[3].urlImage).into(imageButton4)
        mViewRoot.findViewById<TextView>(R.id.fourtext).text = playlists[3].title

    }

    fun initBanner(source: String) {
        //Khi lay duoc ma nguon xong thi phai loc ra bang regex
        val p = Pattern.compile("id=\"feature\"(\\w|\\W)+?section")
        val m = p.matcher(sourceWeb)
        var sourceFill = ""
        if (m.find())
            sourceFill = m.group(0)
        //Rut gon xong roi thi bat dau loc image thoi
        listImage.clear()
        val n = Pattern.compile("<li(\\w|\\W)+?href=\"((\\w|\\W)+?)\"(\\w|\\W)+?title=\"((\\w|\\W)+?)\"(\\w|\\W)+?src=\"((\\w|\\W)+?)\"").matcher(sourceFill)
        while (n.find()) {
            listImage.add(DataWebModel(n.group(8), n.group(2).replace("http:", "https:", false), n.group(5)))
        }
        val adapter = AdapterBanner(listImage, this.context!!, mContextAc)


        viewPager = view!!.findViewById(R.id.viewpagerBannerId)

        viewPager.adapter = adapter

        val circleIndicator: CircleIndicator = mViewRoot.findViewById(R.id.cirbanner)
        circleIndicator.setViewPager(viewPager)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContextAc = context
        listener = mContextAc as OnClickInterface
    }


}// Required empty public constructor


