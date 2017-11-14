package com.example.xon.myapplication

import android.media.MediaPlayer
import com.example.xon.myapplication.Model.DetailModel

/**
 * Created by XON on 11/11/2017.
 * Huong dan dung singleton
 */
class SingletonPlayMusic//*********Cau truc singleton******************
private constructor() {

    private object Holder {
        val INSTANCE = SingletonPlayMusic()
    }

    companion object {
        val instance: SingletonPlayMusic by lazy { Holder.INSTANCE }
    }

    //*********************************************
    lateinit var mPlayer: MediaPlayer
    var mSource: String = ""
    var mPlaybackPosition = 0
    lateinit var mMusics: ArrayList<DetailModel>
    lateinit var mCurrentMusic: DetailModel

    fun initSingleton() {
        mPlayer = MediaPlayer()
        mMusics = ArrayList<DetailModel>()
    }

    //Tiep tuc
    fun continuePlayMusic() {
        mPlayer.seekTo(mPlaybackPosition)
        mPlayer.start()
    }

    fun playMusic() {
        killPlayer()

        //Lay nguon va play
        mPlayer.setDataSource(mCurrentMusic.source.Q128)
        mPlayer.prepare()
        mPlayer.start()
    }

    fun setAndPlayMusic(music: DetailModel) {
        mCurrentMusic = music
        playMusic()
    }

    fun pauseMusic() {
        if (mPlayer.isPlaying) {
            mPlaybackPosition = mPlayer.currentPosition
            mPlayer.pause()
        } else {
            continuePlayMusic()
        }
    }

    fun prevousMusic() {
        if (mMusics.size != 0) {
            killPlayer()

            mCurrentMusic = mMusics.get(mMusics.indexOf(mCurrentMusic) - 1)

            playMusic()
        }
    }


    fun nextMusic() {
        if (mMusics.size != 0) {
            mCurrentMusic = mMusics.get(mMusics.indexOf(mCurrentMusic) + 1)

            playMusic()
        }
    }

    fun restartMusic() {

    }


    fun killPlayer() {
        mPlayer.release()
        mPlayer = MediaPlayer()
    }

    init {
        initSingleton()
    }
}