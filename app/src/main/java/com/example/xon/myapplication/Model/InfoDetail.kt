package com.example.xon.myapplication.Model

import java.util.*

/**
 * Created by XON on 11/4/2017.
 */
public class InfoDetail {
    var Id: String = ""
    var Name: String = ""
    var Title: String = ""
    var Code: String = ""
    var Artists: ArrayList<Artist> = ArrayList()
    var ArtistsName: String = ""
    var Performer: String = ""
    var Type: String = ""
    var Link: String = ""
    var Lyric: String = ""
    var Thumbnail: String =""
    var MVLink: String =""
    lateinit var ArtistInf : ArtistInfo
    lateinit var Sources: Quality
    var STT : String = ""

    constructor()

    // var someProperty: String = ""
   //     get() = field
   //     set(value) { field = value }





}