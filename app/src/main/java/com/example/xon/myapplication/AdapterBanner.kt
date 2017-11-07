package com.example.xon.myapplication

import android.content.Context
import android.content.Intent
import android.media.Image
import android.provider.ContactsContract
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

/**
 * Created by XON on 10/31/2017.
 */
class AdapterBanner : PagerAdapter {

    var lstImages : ArrayList<DataWebModel> = ArrayList()
    lateinit var context : Context
    lateinit var inflater : LayoutInflater


    override fun instantiateItem(container: ViewGroup?, position: Int): Any {

        //Inflate no nhan them cai imagebanner vao fragment.xml tung cai 1
        var view : View = inflater.inflate(R.layout.imagebanner,container,false)

        //Trong fragment.xml da nhan duoc imageButton tu imageBanner.xml
        var imageButton : ImageButton = view.findViewById<ImageButton>(R.id.imageId)
        var txtTitle : TextView = view.findViewById<TextView>(R.id.titleid)
        //imageButton.setImageResource(lstImages.get(position))
        Picasso.with(view.context).load(lstImages.get(position).urlImage).into(imageButton)
        txtTitle.setText(lstImages.get(position).title)

        imageButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                Log.e("Chao",lstImages.get(position).toString())

                //Lam cac cong viec dua dia chi vao day
                //Tao intent
                var intent  = Intent(view.context,DetailActivity::class.java)
                intent.putExtra("URLWeb",lstImages.get(position).urlWeb)
                view.context.startActivity(intent)
            }
        })

        container!!.addView(view)
        return view
    }

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
       return view!!.equals(`object`)
    }

    override fun getCount(): Int {
        return lstImages.size
    }

    constructor(lstImages: ArrayList<DataWebModel>, context: Context) : super() {
        this.lstImages = lstImages
        this.context = context
        inflater = LayoutInflater.from(context)
    }


    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container!!.removeView(`object` as View)
    }

}