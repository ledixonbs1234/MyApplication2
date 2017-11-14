package com.example.xon.myapplication

import android.media.MediaPlayer

/**
 *
 * Created by XON on 11/10/2017.
 */
class PlayMusic {


    lateinit var source: String
    var mediaPlayer: MediaPlayer


    fun playFromUrl(url: String) {
        killPlayer()

        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource("https:" + url)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    fun pauseMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    fun killPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    init {
        mediaPlayer = MediaPlayer()
    }


}