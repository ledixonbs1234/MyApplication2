package com.example.xon.myapplication.InterfaceView

import com.example.xon.myapplication.Model.DataSongManager

/**
 * Created by Administrator on 11/18/2017.
 */
interface OnClickInterface {
    fun openDetailOrPlayerFragment(tag: String)
    fun openTypeFragment()
    fun openTypeDetailFragment(urlType: String)
    fun openBarMusic(data: DataSongManager)
}