package com.example.xon.myapplication.Adapter

import android.media.MediaPlayer
import com.example.xon.myapplication.Model.DataSongManager

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
    lateinit var mMusics: ArrayList<DataSongManager>
    lateinit var mCurrentMusic: DataSongManager
    var defaultQuality: String = "128"

    fun initSingleton() {
        mPlayer = MediaPlayer()
        mPlayer.setOnCompletionListener {
            object : MediaPlayer.OnCompletionListener {

                override fun onCompletion(p0: MediaPlayer?) {
                    if (mMusics.size != 0) {
                        mCurrentMusic = mMusics.get(mMusics.indexOf(mCurrentMusic))
                        playMusic()
                    }
                }
            }
        }
        mMusics = ArrayList<DataSongManager>()
    }

    fun getDurationMediaPlayer(): Int {
        return mPlayer.duration
    }

    //Tiep tuc
    fun continuePlayMusic() {
        mPlayer.seekTo(mPlaybackPosition)
        mPlayer.start()
    }

    fun playMusic() {
        killPlayer()

        when (defaultQuality) {
            "128" -> {
                mPlayer.setDataSource(mCurrentMusic.mainSong.source.q128)
            }
            "320" -> {
                if (!mCurrentMusic.mainSong.source.q320.isNullOrEmpty()) {
                    mPlayer.setDataSource(mCurrentMusic.mainSong.source.q320)
                } else {
                    mPlayer.setDataSource(mCurrentMusic.mainSong.source.q128)
                }
            }
            "500" -> {
                if (!mCurrentMusic.mainSong.source.q500.isNullOrEmpty()) {
                    mPlayer.setDataSource(mCurrentMusic.mainSong.source.q500)
                } else if (!mCurrentMusic.mainSong.source.q320.isNullOrEmpty()) {
                    mPlayer.setDataSource(mCurrentMusic.mainSong.source.q320)
                } else {
                    mPlayer.setDataSource(mCurrentMusic.mainSong.source.q128)
                }
            }
            "lossless" -> {
                if (!mCurrentMusic.mainSong.source.lossless.isNullOrEmpty()) {
                    mPlayer.setDataSource(mCurrentMusic.mainSong.source.lossless)
                } else if (!mCurrentMusic.mainSong.source.q500.isNullOrEmpty()) {
                    mPlayer.setDataSource(mCurrentMusic.mainSong.source.q500)
                } else if (!mCurrentMusic.mainSong.source.q320.isNullOrEmpty()) {
                    mPlayer.setDataSource(mCurrentMusic.mainSong.source.q320)
                } else {
                    mPlayer.setDataSource(mCurrentMusic.mainSong.source.q128)
                }
            }
        }
        //Lay nguon va play
        mPlayer.prepare()
        mPlayer.start()
    }

    fun setAndPlayMusic(music: DataSongManager) {
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
        if (mMusics.size != 0 && mMusics.indexOf(mCurrentMusic) >= 1) {
            killPlayer()

            mCurrentMusic = mMusics.get(mMusics.indexOf(mCurrentMusic) - 1)

            playMusic()
        }
    }


    fun nextMusic() {
        if (mMusics.size != 0 && mMusics.indexOf(mCurrentMusic) < mMusics.size) {
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