package com.example.xon.myapplication

import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity()  {

    lateinit var viewPager : ViewPager
    lateinit var tabLayout : TabLayout
    lateinit var mFragmentOne: FragmentMain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //New
        initView()
        //

        //viewPager = findViewById(R.id.viewpagerid)
//
        //var adapter : AdapterViewPager = AdapterViewPager(supportFragmentManager)
        // adapter.addFragment(OnlineFragment(),"Online")
        // adapter.addFragment(OfflineFragment(),"Offline")
        // viewPager.setAdapter(adapter)
//
//
        // tabLayout = findViewById(R.id.tabs)

        // tabLayout.setupWithViewPager(viewPager)
    }

    fun initView() {
        //Khoi tao fragment Main
        //if (mFragmentOne == null) {
        //    mFragmentOne = FragmentMain()
        // }
        mFragmentOne = FragmentMain()
        //Show Fragment
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_activity, mFragmentOne, mFragmentOne.javaClass.name)
                .commit()

    }
}

public class TaskerExample : AsyncTask<String,String,String>(){
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
            if (responseCode == 200){
                val stream: InputStream = connect.inputStream
                Result = ConvertoString(stream)
            }
        }catch (Ex : Exception)
        {
            Log.d("","Exception " + Ex.message)
        }
        return Result
    }

    private fun ConvertoString(stream: InputStream): String {
        var result = ""
        val isReader = InputStreamReader(stream)
        val bReader = BufferedReader(isReader)

        var temp : String?
        try {
            while (true){
                temp = bReader.readLine()
                if (temp == null)
                    break
                result += temp

            }
        }catch (ex: Exception){
            Log.e("Tag","Error checking data"+ ex.printStackTrace())
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
