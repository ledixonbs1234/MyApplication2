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
import com.example.xon.myapplication.Model.DataSongFullSimple
import com.example.xon.myapplication.Model.DataSongManager
import com.example.xon.myapplication.Model.DataSongQualitySimple
import com.example.xon.myapplication.Model.Qualities
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
        var view = inflater.inflate(R.layout.item, parent, false)
        //view.setOnClickListener(mOnclickListener)
        return RecyclerViewHolder(inflater.context, view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder?, position: Int) {

        //Lấy link từ ChiaseNhac
        if (datas[position].tempSongs == null) {
            datas[position].tempSongs = TaskerDetail().execute(datas[position].id).get()

            //Thuc hien kiem tra va ra cai moi nhat
            for (song in datas[position].tempSongs!!) {
                if (song.songName.toLowerCase() == datas[position].mainSong.songName.toLowerCase() &&
                        song.artist.replace(';', ',', false).toLowerCase() == datas[position].mainSong.artist.toLowerCase()) {

                    //Thuc hien chuyen bai hat temp da click sang bai hat chinh
                    datas[position].mainSong.artist = song.artist
                    datas[position].mainSong.songName = song.songName
                    datas[position].mainSong.linkWebCSN = song.linkWebDownload
                    datas[position].highQualityShow = song.qualityShow
                    datas[position].linkWebDownloadCSN = song.linkWebDownload
                }
            }
        }
        if (datas[position].mainSong.songName.length > 20) {
            holder!!.songnameid!!.text = datas[position].mainSong.songName.substring(0, 20) + "..."
        } else
            holder!!.songnameid!!.text = datas[position].mainSong.songName

        holder.tencasiid!!.text = datas[position].mainSong.artist
        holder.countid!!.text = (position + 1).toString()
        holder.quality.text = covertQualitiesToText(datas[position].highQualityShow)
    }

    private fun covertQualitiesToText(quality: Qualities): String {
        when (quality) {
            Qualities.q128 -> return "128 Kbps"
            Qualities.q320 -> return "128 Kbps"
            Qualities.q500 -> return "500 Kbps"
            Qualities.Lossless -> return "Lossless"
        }
    }


    override fun getItemCount(): Int {
        return datas.size
    }

    inner class RecyclerViewHolder : RecyclerView.ViewHolder {

        var songnameid: TextView? = null
        var tencasiid: TextView? = null
        var countid: TextView? = null
        var download: ImageButton
        var contexttemp: Context
        var quality: TextView

        constructor(context: Context, itemView: View) :
                super(itemView) {
            songnameid = itemView.findViewById(R.id.songnameid)
            tencasiid = itemView.findViewById(R.id.tencasiid)
            countid = itemView.findViewById(R.id.countid)
            download = itemView.findViewById(R.id.btndownloadmusic)
            quality = itemView.findViewById(R.id.qualitydetail)

            contexttemp = context

            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    if (!v.isActivated) {
                        var detail = datas.get(adapterPosition)
                        Toast.makeText(contexttemp, songnameid!!.text, Toast.LENGTH_LONG).show()
                        var data: ArrayList<DataSongFullSimple> = arrayListOf()
                        datas.forEach { n -> data.add(n.mainSong) }
                        //Lay list neu co
                        singleton.mMusics = data
                        //Play music
                        singleton.setAndPlayMusic(detail.mainSong)

                        //callback
                        listener.openDetailOrPlayerFragment(detail.mainSong.linkMp3, detail.linkWebDownloadCSN)

                        //callback
                        //listener.openBarMusic(detail)
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
                            mainSong.linkWebCSN = detail.get(p1).linkWebDownload
                            songManager.linkWebDownloadCSN = detail.get(p1).linkWebDownload
                            songManager.highQualityShow = detail.get(p1).qualityShow

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