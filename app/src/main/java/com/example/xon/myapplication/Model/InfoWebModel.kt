package com.example.xon.myapplication.Model

/**
 * Created by XON on 11/3/2017.
 */
class InfoWebModel {
    private var err: Int
    private var msg: String
    private var Data: DataWeb

    constructor(err: Int, msg: String, Data: DataWeb) {
        this.err = err
        this.msg = msg
        this.Data = Data
    }


}