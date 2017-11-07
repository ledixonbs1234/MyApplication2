package com.example.xon.myapplication.Model

/**
 * Created by XON on 11/5/2017.
 */
public class DetailModel {
    public var tenBaiHat : String
    public var caSi : String
    public var linkBaiHat : String
    public var lyric : String
    public var stt : String

    constructor(tenBaiHat: String, caSi: String, linkBaiHat: String, lyric: String, stt: String) {
        this.tenBaiHat = tenBaiHat
        this.caSi = caSi
        this.linkBaiHat = linkBaiHat
        this.lyric = lyric
        this.stt = stt
    }
}