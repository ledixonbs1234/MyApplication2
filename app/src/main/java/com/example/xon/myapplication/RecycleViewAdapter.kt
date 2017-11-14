package com.example.xon.myapplication

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.xon.myapplication.InterfaceView.OnClickRec
import com.example.xon.myapplication.Model.DetailModel


/**
 *
 * Created by XON on 11/2/2017.
 */
class RecycleViewAdapter : RecyclerView.Adapter<RecycleViewAdapter.RecyclerViewHolder> {
    private var data : ArrayList<DetailModel> = ArrayList<DetailModel>()
    lateinit var playMusic: PlayMusic
    var singleton: SingletonPlayMusic
    lateinit var listener: OnClickRec
    //private final var mOnclickListener : View.OnClickListener = View.OnClickListener()

    constructor(data: ArrayList<DetailModel>, context: Context) : super() {
        this.data = data
        singleton = SingletonPlayMusic.instance

        try {
            listener = context as OnClickRec
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
        holder!!.songnameid!!.setText(data[position].tenBaiHat)
        holder.tencasiid!!.setText(data[position].caSi)
        holder.countid!!.setText(data[position].stt)
         }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerViewHolder : RecyclerView.ViewHolder {

        var songnameid: TextView? = null
        var tencasiid: TextView? = null
        var countid : TextView? = null
        var contexttemp :Context

        constructor(context: Context, itemView: View):
            super(itemView)
        {
            songnameid = itemView.findViewById(R.id.songnameid)
            tencasiid = itemView.findViewById(R.id.tencasiid)
            countid = itemView.findViewById(R.id.countid)
            contexttemp = context
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v:View){
                    var detail : DetailModel = data.get(adapterPosition)
                    Toast.makeText(contexttemp,songnameid!!.getText(),Toast.LENGTH_LONG).show()

                    //Lay list neu co
                    singleton.mMusics = data
                    //Play music
                    singleton.setAndPlayMusic(detail)

                    //callback
                    listener.onClickRecView()
                }
            })
        }

    }

}