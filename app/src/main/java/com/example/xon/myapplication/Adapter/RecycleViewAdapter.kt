package com.example.xon.myapplication.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.xon.myapplication.InterfaceView.OnClickInterface
import com.example.xon.myapplication.Model.DataSongManager
import com.example.xon.myapplication.Model.DataSongQualitySimple
import com.example.xon.myapplication.R


/**
 *
 * Created by XON on 11/2/2017.
 */
class RecycleViewAdapter : RecyclerView.Adapter<RecycleViewAdapter.RecyclerViewHolder> {
    private var datas: ArrayList<DataSongManager>
    var singleton: SingletonPlayMusic
    var listener: OnClickInterface

    constructor(datas: ArrayList<DataSongManager>, context: Context) : super() {
        this.datas = datas

        singleton = SingletonPlayMusic.instance

        try {
            listener = context as OnClickInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement on View Selected")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerViewHolder {
        var inflater = LayoutInflater.from(parent!!.context)
        var view  = inflater.inflate(R.layout.item,parent,false)
        //view.setOnClickListener(mOnclickListener)
        return RecyclerViewHolder(inflater.context,view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder?, position: Int) {
        holder!!.songnameid!!.text = datas[position].mainSong.songName
        holder.tencasiid!!.text = datas[position].mainSong.artist
        holder.countid!!.text = (position + 1).toString()
        holder.quality!!.text = datas[position].mainSong.source.getHighQuality()
         }

    override fun getItemCount(): Int {
        return datas.size
    }

    inner class RecyclerViewHolder : RecyclerView.ViewHolder {

        var songnameid: TextView? = null
        var tencasiid: TextView? = null
        var countid : TextView? = null
        var download: ImageButton
        var contexttemp :Context
        var quality: TextView

        constructor(context: Context, itemView: View):
            super(itemView)
        {
            songnameid = itemView.findViewById(R.id.songnameid)
            tencasiid = itemView.findViewById(R.id.tencasiid)
            countid = itemView.findViewById(R.id.countid)
            download = itemView.findViewById(R.id.btndownloadmusic)
            quality = itemView.findViewById(R.id.qualitydetail)

            contexttemp = context

            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v:View){
                    if (!v.isActivated) {
                        var detail = datas.get(adapterPosition)
                        Toast.makeText(contexttemp, songnameid!!.text, Toast.LENGTH_LONG).show()

                        //Lay list neu co
                        singleton.mMusics = datas
                        //Play music
                        singleton.setAndPlayMusic(detail)

                        //callback
                        listener.openBarMusic(detail)
                    }
                }
            })

            download.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {

                    //Tao sepuence item
                    //val Animals = datass.toArray(arrayOfNulls<String>(datass.size))

                    var dialog: AlertDialog.Builder = AlertDialog.Builder(itemView.context)
                    dialog.setTitle("Chọn bài hát")

                    //var inflater : LayoutInflater = LayoutInflater.from(itemView.context)
                    //var dialogView : View = inflater.inflate(R.layout.dialog_item,null)
                    //dialog.setView(dialogView)
                    var songManager = datas.get(adapterPosition)
                    var detail: ArrayList<DataSongQualitySimple>? = songManager.tempSongs
                    var mainSong = songManager.mainSong

                    dialog.setAdapter(AdapterDialog(detail!!, itemView.context), object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            Toast.makeText(itemView.context, "Vi tri thu" + p1, Toast.LENGTH_LONG).show()

                            //Thuc hien chuyen bai hat temp da click sang bai hat chinh
                            mainSong.artist = detail.get(p1).artist
                            mainSong.songName = detail.get(p1).songName
                            mainSong.source = detail.get(p1).quality
                        }
                    })

                    //dialogView.findViewById<TextView>(R.id.songnamedialog).text = "Test"
                    //dialogView.findViewById<TextView>(R.id.tencasidialog).text = "Test dcccc"

                    //dialog.setItems(Animals,object : DialogInterface.OnClickListener{
                    //    override fun onClick(p0: DialogInterface?, p1: Int) {
                    //        var text : String = Animals[p1].toString()
                    //    }
                    //})

                    //Tao dialog
                    var alertDialogObject: AlertDialog = dialog.create()
                    //Hien len
                    alertDialogObject.show()
                }
            })
        }

    }

}