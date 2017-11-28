package com.example.xon.myapplication.View

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import com.example.xon.myapplication.Adapter.SingletonPlayMusic
import com.example.xon.myapplication.InterfaceView.OnClickInterface
import com.example.xon.myapplication.Model.DataSongManager
import com.example.xon.myapplication.R
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity(), OnClickInterface, View.OnClickListener {

    lateinit var mFragmentMain: FragmentMain
    lateinit var mFragmentDetail: FragmentDetail
    lateinit var mFragmentPlayer: FragmentPlayer
    lateinit var mFragmentType: FragmentType
    lateinit var mFragmentDetailType: FragmentDetailType
    lateinit var singleton: SingletonPlayMusic
    lateinit var mPlayPause: ImageButton
    lateinit var mNext: ImageButton
    lateinit var mPrevous: ImageButton
    lateinit var mTenBaiHat: TextView
    lateinit var mTenCaSi: TextView
    lateinit var mFrameBar: FrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            //New
            initView()
        } else {
            //Thuc hien cac len trong nay
        }

    }


    fun getSourceJsonWeb(url: String): String {
        return TaskerExample().execute(url).get()
    }


    override fun openDetailOrPlayerFragment(linkMp3: String, linkWebCSN: String?) {

        //Thực hiện kiểm tra link có phải album hay ko
        if (linkMp3.indexOf("album") != -1) {
            mFragmentDetail = FragmentDetail()
            //Chuyen du lieu vao fragment
            val bundle = Bundle()
            bundle.putString("URL", linkMp3)
            mFragmentDetail.arguments = bundle

            supportFragmentManager.beginTransaction()
                    .replace(R.id.main_activity, mFragmentDetail, mFragmentDetail.javaClass.name)
                    .setCustomAnimations(R.anim.anim_detail_in, R.anim.anim_detail_out)
                    .addToBackStack("detail")
                    .commit()
        } else {
            openPlayer(linkMp3, linkWebCSN)
        }


        //Thực hiện lấy mã token
        //var n = Pattern.compile("zplayerjs(\\w|\\W)+?key=((\\W|\\w)+?)\"").matcher(getSourceJsonWeb(data))
        // var token : String =""
        //while (n.find()) {
        //    token = n.group(2)
        //}


    }

    fun initView() {


        //Khoi tao cac view
        mNext = findViewById(R.id.next)
        mNext.setOnClickListener(this)

        mPlayPause = findViewById(R.id.playpause)
        mPlayPause.setOnClickListener(this)

        mPrevous = findViewById(R.id.prevous)
        mPrevous.setOnClickListener(this)

        mTenBaiHat = findViewById(R.id.tenbaihat)
        mTenCaSi = findViewById(R.id.tencasi)
        mFrameBar = findViewById(R.id.farmbar)
        mFrameBar.setOnClickListener(this)

        mTenBaiHat.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                openPlayer(singleton.mCurrentMusic.linkMp3, null)
            }
        })
        //get instance singleton
        singleton = SingletonPlayMusic.instance

        mFragmentMain = FragmentMain()
        //Show Fragment
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_activity, mFragmentMain, mFragmentMain.javaClass.name)
                .addToBackStack(null)
                .commit()

    }

    fun openPlayer(linkMp3: String, linkWebCSN: String?) {
        //Neu khong co file nguon thi vao fragment Player
        mFrameBar.visibility = View.GONE

        mFragmentPlayer = FragmentPlayer()

        //Chuyen du lieu vao fragment
        val bundle = Bundle()
        bundle.putString("URLMP3", linkMp3)
        bundle.putString("URLWEBCSN", linkWebCSN)
        mFragmentPlayer.arguments = bundle

        supportFragmentManager.beginTransaction().replace(R.id.main_activity, mFragmentPlayer, "FragmentPlayer")
                .addToBackStack(null)
                .commit()
    }

    override fun openTypeFragment() {
        mFragmentType = FragmentType()
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_activity, mFragmentType, mFragmentType.javaClass.name)
                .addToBackStack(null)
                .commit()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.prevous -> {
                singleton.prevousMusic()
                mTenBaiHat.text = singleton.mCurrentMusic.songName
                mTenCaSi.text = singleton.mCurrentMusic.artist
            }
            R.id.next -> {
                singleton.nextMusic()
                mTenBaiHat.text = singleton.mCurrentMusic.songName
                mTenCaSi.text = singleton.mCurrentMusic.artist
            }
            R.id.playpause -> {
                singleton.pauseMusic()
                mTenBaiHat.text = singleton.mCurrentMusic.songName
                mTenCaSi.text = singleton.mCurrentMusic.artist
                if (singleton.mPlayer.isPlaying) {
                    mPlayPause.setImageResource(R.drawable.ic_pause_black_24dp)
                } else {
                    mPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                }
            }
            R.id.farmbar -> {
                openPlayer(singleton.mCurrentMusic.linkMp3, null)
            }
        }
    }

    override fun openBarMusic(data: DataSongManager) {
        //Thuc hien show cai frameLayoutBar
        mFrameBar.visibility = View.VISIBLE

        mTenBaiHat.text = singleton.mCurrentMusic.songName
        mTenCaSi.text = singleton.mCurrentMusic.artist

        if (singleton.mPlayer.isPlaying) {
            mPlayPause.setImageResource(R.drawable.ic_pause_black_24dp)
        } else {
            mPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        }

        ////Neu khong co file nguon thi vao fragment Player
        //mFrameBar.visibility = View.GONE

        //mFragmentPlayer = FragmentPlayer()

        ////Chuyen du lieu vao fragment
        //val bundle = Bundle()
        //bundle.putString("URLPlayer",music.linkBaiHat )
        //mFragmentPlayer.arguments = bundle

        //supportFragmentManager.beginTransaction().replace(R.id.main_activity,mFragmentPlayer,"FragmentPlayer")
        //        .addToBackStack(null)
        //        .commit()


    }

    override fun openTypeDetailFragment(urlType: String) {
        mFragmentDetailType = FragmentDetailType()

        //Chuyen du lieu vao fragment
        val bundle = Bundle()
        bundle.putString("URLType", urlType)
        mFragmentDetailType.arguments = bundle

        supportFragmentManager.beginTransaction().replace(R.id.main_activity, mFragmentDetailType, "FragmentDetailType")
                .addToBackStack(null)
                .commit()
    }
}

class TaskerExample : AsyncTask<String, String, String>() {
    override fun doInBackground(vararg params: String?): String? {
        var Result = ""
        try {

            val URL = URL(params[0])
            //params[0]!!.indexOf("https", 0, false) != -1
            //val URL = URL("http://chiasenhac.vn/download2.php?v1=1846&v2=1&v3=1YJD2DG-IGbdYXMX&v4=128&v5=sss.mp3")
            if (params[0]!!.indexOf("https", 0, false) != -1) {
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

}
