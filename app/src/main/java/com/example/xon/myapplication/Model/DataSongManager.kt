package com.example.xon.myapplication.Model

/**
 * Created by Administrator on 11/21/2017.
 */
class DataSongManager {
    public var id: String
    public var mainSong: DataSongFullSimple
    public var tempSongs: ArrayList<DataSongQualitySimple>?

    constructor(id: String, mainSong: DataSongFullSimple, tempSongs: ArrayList<DataSongQualitySimple>?) {
        this.id = id
        this.mainSong = mainSong
        this.tempSongs = tempSongs
    }
}