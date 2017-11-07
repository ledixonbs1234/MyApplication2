package com.example.xon.myapplication

/**
 * Created by XON on 11/2/2017.
 */
public class DataWebModel {
    val urlWeb : String
    var urlImage : String
    val title : String

    constructor(urlImage: String,urlWeb : String, title: String) {
        this.urlImage = urlImage
        this.urlWeb = urlWeb
        this.title = title
    }

}