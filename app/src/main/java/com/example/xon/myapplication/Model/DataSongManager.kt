package com.example.xon.myapplication.Model

/**
 * Created by Administrator on 11/21/2017.
 */
class DataSongManager {
    var id: String
    var mainSong: DataSongFullSimple
    var tempSongs: ArrayList<DataSongQualitySimple>?
    var highQualityShow: Qualities = Qualities.q128
    var linkWebDownloadCSN: String?

    constructor(id: String, mainSong: DataSongFullSimple, tempSongs: ArrayList<DataSongQualitySimple>?, linkWebDownloadCSN: String?) {
        this.id = id
        this.mainSong = mainSong
        this.tempSongs = tempSongs
        this.linkWebDownloadCSN = linkWebDownloadCSN
    }
}