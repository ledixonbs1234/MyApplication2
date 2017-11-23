package com.example.xon.myapplication.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
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
 * Created by Administrator on 11/17/2017.
 */
class AdapterViewType : RecyclerView.Adapter<AdapterViewType.ViewHoler> {

    var mStrings: ArrayList<DataWebModel>
    lateinit var mView: View
    var listener: OnClickInterface

    override fun onBindViewHolder(holder: ViewHoler?, position: Int) {
        holder!!.title.text = mStrings[position].title
        Picasso.with(mView.context).load(mStrings[position].urlImage).into(holder.urlImage)
    }

    override fun getItemCount(): Int {
        return mStrings.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHoler {
        var inflater = LayoutInflater.from(parent!!.context)
        mView = inflater.inflate(R.layout.type_item, parent, false)

        return ViewHoler(mView, inflater.context)
    }

    constructor(strings: ArrayList<DataWebModel>, context: Context) : super() {
        mStrings = strings
        try {
            listener = context as OnClickInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "class just implement to View")
        }
    }


    inner class ViewHoler : RecyclerView.ViewHolder {
        var title: TextView
        var urlImage: ImageButton
        var mContextTemp: Context

        constructor(itemView: View?, context: Context) : super(itemView) {

            urlImage = itemView!!.findViewById<ImageButton>(R.id.imagetype)
            title = itemView.findViewById<TextView>(R.id.typename)
            mContextTemp = context

            //Thiet lap Listener
            urlImage.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    var type = mStrings.get(adapterPosition)
                    listener.openTypeDetailFragment(type.urlWeb)
                }
            })
        }
    }
}