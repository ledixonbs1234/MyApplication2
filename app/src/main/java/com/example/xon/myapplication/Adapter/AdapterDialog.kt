package com.example.xon.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.xon.myapplication.Model.DataSongQualitySimple
import com.example.xon.myapplication.R

class AdapterDialog : BaseAdapter {

    lateinit var datas: ArrayList<DataSongQualitySimple>
    lateinit var context: Context

    constructor()

    constructor(datas: ArrayList<DataSongQualitySimple>, context: Context) {
        this.datas = datas
        this.context = context
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view: View
        var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflater.inflate(R.layout.dialog_item, null)

        view.findViewById<TextView>(R.id.songnamedialog).text = datas.get(p0).songName
        view.findViewById<TextView>(R.id.tencasidialog).text = datas.get(p0).artist
        view.findViewById<TextView>(R.id.qualitydialog).text = datas.get(p0).qualityShow.name
        view.findViewById<TextView>(R.id.countdialogid).text = (p0 + 1).toString()
        return view
    }

    override fun getItem(p0: Int): Any {
        return datas.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return datas.size
    }
}