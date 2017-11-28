package com.example.xon.myapplication.Model

/**
 * Created by XON on 11/4/2017.
 */
class Quality {
    var q128: String = ""
    var q320: String = ""
    var q500: String = ""
    var lossless: String = ""

    constructor(q128: String, q320: String, q500: String, lossless: String) {
        this.q128 = q128
        this.q320 = q320
        this.q500 = q500
        this.lossless = lossless
    }

    fun getHighQuality(): Qualities {
        if (!lossless.isNullOrEmpty()) {
            return Qualities.Lossless
        } else if (!q500.isNullOrEmpty())
            return Qualities.q500
        else if (!q320.isNullOrEmpty())
            return Qualities.q320
        else
            return Qualities.q128
    }

    constructor()

}