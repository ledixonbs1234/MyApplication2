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

    fun getHighQuality(): String {
        if (!lossless.isNullOrEmpty()) {
            return "Lossless"
        } else if (!q500.isNullOrEmpty())
            return "500 Kbs"
        else if (!q320.isNullOrEmpty())
            return "320 Kbs"
        else
            return "128 Kbs"
    }

    constructor()

}