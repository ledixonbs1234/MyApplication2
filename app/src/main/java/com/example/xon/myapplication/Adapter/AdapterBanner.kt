package com.example.xon.myapplication.Adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.xon.myapplication.InterfaceView.OnClickInterface
import com.example.xon.myapplication.Model.DataWebModel
import com.example.xon.myapplication.R
import com.squareup.picasso.Picasso

/**
 *
 * Created by XON on 10/31/2017.
 */
class AdapterBanner(var lstImages: ArrayList<DataWebModel>, var context: Context, contextAc: Context) : PagerAdapter() {

    var inflater: LayoutInflater
    // Lam 1 cai activity tai day
    var listener: OnClickInterface

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        //Inflate no nhan them cai imagebanner vao fragment.xml tung cai 1
        val view: View = inflater.inflate(R.layout.imagebanner, container, false)

        //Trong fragment.xml da nhan duoc imageButton tu imageBanner.xml
        val imageButton: ImageButton = view.findViewById<ImageButton>(R.id.imageId)
        val txtTitle: TextView = view.findViewById<TextView>(R.id.titleid)
        //imageButton.setImageResource(lstImages.get(position))
        Picasso.with(view.context).load(lstImages.get(position).urlImage).into(imageButton)
        txtTitle.text = lstImages.get(position).title

        imageButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                Log.e("Chao",lstImages.get(position).toString())

                //Lam cac cong viec dua dia chi vao day
                //Tao intent
                //var intent  = Intent(view.context,DetailActivity::class.java)
                //intent.putExtra("URLWeb",lstImages.get(position).urlWeb)
                //view.context.startActivity(intent)

                listener.openDetailOrPlayerFragment(lstImages.get(position).urlWeb)
            }
        })

        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun getCount(): Int {
        return lstImages.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    init {
        inflater = LayoutInflater.from(context)
        try {
            listener = contextAc as OnClickInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement on View Selected")
        }
    }


}