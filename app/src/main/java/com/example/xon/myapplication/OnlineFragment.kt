package com.example.xon.myapplication


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * A simple [Fragment] subclass.
 */
class OnlineFragment : Fragment() {

    //Tao list
    var listImage : ArrayList<DataWebModel> = ArrayList()
    lateinit var viewPager : ViewPager

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_online, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // listImage.add(R.drawable.d1)
       // listImage.add(R.drawable.d2)
       // listImage.add(R.drawable.d4)
       // listImage.add(R.drawable.d3)

        var source : String = "https://mp3.zing.vn"
        var sourceWeb : String

        sourceWeb =  TaskerExample().execute(source).get()

        //Khi lay duoc ma nguon xong thi phai loc ra bang regex
        var p =Pattern.compile("id=\"feature\"(\\w|\\W)+?section")
        var m = p.matcher(sourceWeb)
        var sourceFill : String=""
        if (m.find())
            sourceFill= m.group(0)
        //Rut gon xong roi thi bat dau loc image thoi
        listImage.clear()
        var n =Pattern.compile("<li(\\w|\\W)+?href=\"((\\w|\\W)+?)\"(\\w|\\W)+?title=\"((\\w|\\W)+?)\"(\\w|\\W)+?src=\"((\\w|\\W)+?)\"").matcher(sourceFill)
        while (n.find()){
            listImage.add(DataWebModel(n.group(8),n.group(2),n.group(5)))
        }


        var adapter : AdapterBanner = AdapterBanner(listImage,this.context)

        viewPager = view!!.findViewById<ViewPager>(R.id.viewpagerBannerId)

        viewPager.setAdapter(adapter)




    }
}// Required empty public constructor
