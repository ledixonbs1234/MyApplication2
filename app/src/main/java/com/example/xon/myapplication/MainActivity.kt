package com.example.xon.myapplication

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import com.example.xon.myapplication.InterfaceView.OnClickRec
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity(), OnClickAdapterBanner, View.OnClickListener, OnClickRec {

    lateinit var mFragmentMain: FragmentMain
    lateinit var mFragmentDetail: FragmentDetail
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

    fun OpenDetailFragment(data: String) {
        mFragmentDetail = FragmentDetail()

        //Chuyen du lieu vao fragment
        val bundle = Bundle()
        bundle.putString("URL", data)
        mFragmentDetail.arguments = bundle

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_activity, mFragmentDetail, mFragmentDetail.javaClass.name)
                .setCustomAnimations(R.anim.anim_detail_in, R.anim.anim_detail_out)
                .addToBackStack("detail")
                .commit()
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
        mFrameBar = findViewById(R.id.frambar)


        //get instance singleton
        singleton = SingletonPlayMusic.instance

        mFragmentMain = FragmentMain()
        //Show Fragment
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_activity, mFragmentMain, mFragmentMain.javaClass.name)
                .addToBackStack(null)
                .commit()

    }

    override fun onClickButton(tag: String) {
        OpenDetailFragment(tag)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.prevous -> {
                singleton.prevousMusic()
                mTenBaiHat.text = singleton.mCurrentMusic.tenBaiHat
                mTenCaSi.text = singleton.mCurrentMusic.caSi
            }
            R.id.next -> {
                singleton.nextMusic()
                mTenBaiHat.text = singleton.mCurrentMusic.tenBaiHat
                mTenCaSi.text = singleton.mCurrentMusic.caSi
            }
            R.id.playpause -> {
                singleton.pauseMusic()
                mTenBaiHat.text = singleton.mCurrentMusic.tenBaiHat
                mTenCaSi.text = singleton.mCurrentMusic.caSi
                if (singleton.mPlayer.isPlaying) {
                    mPlayPause.setImageResource(R.drawable.ic_pause_black_24dp)
                } else {
                    mPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                }

            }
        }
    }

    override fun onClickRecView() {
        //Thuc hien show cai frameLayoutBar
        mFrameBar.visibility = View.VISIBLE

        mTenBaiHat.text = singleton.mCurrentMusic.tenBaiHat
        mTenCaSi.text = singleton.mCurrentMusic.caSi

        if (singleton.mPlayer.isPlaying) {
            mPlayPause.setImageResource(R.drawable.ic_pause_black_24dp)
        } else {
            mPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        }
    }


}

class TaskerExample : AsyncTask<String, String, String>() {
    override fun doInBackground(vararg params: String?): String? {
        var Result = ""
        try {
            val URL = URL(params[0])
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

    override fun onPreExecute() {
        super.onPreExecute()

    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)


    }

    override fun onProgressUpdate(vararg values: String?) {
        super.onProgressUpdate(*values)
    }
}
