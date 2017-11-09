package com.example.xon.myapplication


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.relex.circleindicator.CircleIndicator
import java.util.regex.Pattern


/**
 * A simple [Fragment] subclass.
 */
class OnlineFragment : Fragment() {

    //Tao list
    var listImage : ArrayList<DataWebModel> = ArrayList()
    lateinit var viewPager : ViewPager
    lateinit var mViewRoot: View

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mViewRoot = inflater!!.inflate(R.layout.fragment_online, container, false)

        return mViewRoot
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // listImage.add(R.drawable.d1)
       // listImage.add(R.drawable.d2)
       // listImage.add(R.drawable.d4)
       // listImage.add(R.drawable.d3)

        var source = "https://mp3.zing.vn"
        var sourceWeb : String

        sourceWeb =  TaskerExample().execute(source).get()

        //Khi lay duoc ma nguon xong thi phai loc ra bang regex
        val p = Pattern.compile("id=\"feature\"(\\w|\\W)+?section")
        val m = p.matcher(sourceWeb)
        var sourceFill = ""
        if (m.find())
            sourceFill= m.group(0)
        //Rut gon xong roi thi bat dau loc image thoi
        listImage.clear()
        val n = Pattern.compile("<li(\\w|\\W)+?href=\"((\\w|\\W)+?)\"(\\w|\\W)+?title=\"((\\w|\\W)+?)\"(\\w|\\W)+?src=\"((\\w|\\W)+?)\"").matcher(sourceFill)
        while (n.find()){
            listImage.add(DataWebModel(n.group(8),n.group(2),n.group(5)))
        }


        val adapter = AdapterBanner(listImage, this.context)

        viewPager = view!!.findViewById<ViewPager>(R.id.viewpagerBannerId)

        viewPager.setAdapter(adapter)

        val circleIndicator: CircleIndicator = mViewRoot.findViewById(R.id.cirbanner)
        circleIndicator.setViewPager(viewPager)




    }
}// Required empty public constructor
