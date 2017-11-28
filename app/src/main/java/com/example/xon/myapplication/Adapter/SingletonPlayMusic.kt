package com.example.xon.myapplication.Adapter

import android.media.MediaPlayer
import android.util.Log
import com.example.xon.myapplication.Model.DataSongFullSimple
import com.example.xon.myapplication.Model.Qualities
import com.example.xon.myapplication.Model.Quality
import com.example.xon.myapplication.View.TaskerExample
import java.io.IOException
import java.util.regex.Pattern

/**
 * Created by XON on 11/11/2017.
 * Huong dan dung singleton
 */
class SingletonPlayMusic //*********Cau truc singleton******************
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
    lateinit var mMusics: ArrayList<DataSongFullSimple>
    lateinit var mCurrentMusic: DataSongFullSimple
    var defaultQuality: Qualities = Qualities.q128

    fun initSingleton() {
        mPlayer = MediaPlayer()
        mPlayer.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
            override fun onCompletion(p0: MediaPlayer?) {
                if (mMusics.size != 0) {
                    nextMusic()
                }
            }
        })
        mMusics = ArrayList<DataSongFullSimple>()
    }

    fun getDurationMediaPlayer(): Int {
        return mPlayer.duration
    }

    fun getCurrentPosition(): Int {
        return mPlayer.currentPosition
    }

    fun setPosition(position: Int) {
        mPlayer.seekTo(position)
        mPlayer.start()
    }

    //Tiep tuc
    fun continuePlayMusic() {
        mPlayer.seekTo(mPlaybackPosition)
        mPlayer.start()
    }

    fun playMusic() {
        killPlayer()


        //Thực hiện kiểm tra nếu chưa có thì tìm kiếm
        when (defaultQuality) {
            Qualities.q128 -> {
                if (!mCurrentMusic.linkWebCSN.isNullOrEmpty()) {
                    mCurrentMusic.source = getSourceCSN(mCurrentMusic.linkWebCSN!!)
                }
            }
            Qualities.q320 -> {
                if (!mCurrentMusic.linkWebCSN.isNullOrEmpty() && mCurrentMusic.source.q320.isNullOrEmpty()) {
                    mCurrentMusic.source = getSourceCSN(mCurrentMusic.linkWebCSN!!)
                }
            }
            Qualities.q500 -> {
                if (!mCurrentMusic.linkWebCSN.isNullOrEmpty() && mCurrentMusic.source.q500.isNullOrEmpty()) {
                    mCurrentMusic.source = getSourceCSN(mCurrentMusic.linkWebCSN!!)
                }
            }
            Qualities.Lossless -> {
                if (!mCurrentMusic.linkWebCSN.isNullOrEmpty() && mCurrentMusic.source.lossless.isNullOrEmpty()) {
                    mCurrentMusic.source = getSourceCSN(mCurrentMusic.linkWebCSN!!)
                }
            }
        }

        when (defaultQuality) {
            Qualities.q128 -> {
                mPlayer.setDataSource(mCurrentMusic.source.q128)
                //var uri = Uri.parse("http://chiasenhac.vn/download2.php?v1=1623&v2=1&v3=1G221Y2-baHeJXIJ&v4=128&v5=Thirst%20For%20Life%20-%20Yanni%20[128kbps_MP3].mp3")
                //mPlayer.setDataSource("http://chiasenhac.vn/download2.php?v1=1623&v2=1&v3=1G221Y2-baHeJXIJ&v4=128&v5=dddd.mp3")
            }
            Qualities.q320 -> {
                if (!mCurrentMusic.source.q320.isNullOrEmpty()) {
                    mPlayer.setDataSource(mCurrentMusic.source.q320)
                } else {
                    mPlayer.setDataSource(mCurrentMusic.source.q128)
                }
            }
            Qualities.q500 -> {
                if (!mCurrentMusic.source.q500.isNullOrEmpty()) {
                    mPlayer.setDataSource(mCurrentMusic.source.q500)
                } else if (!mCurrentMusic.source.q320.isNullOrEmpty()) {
                    mPlayer.setDataSource(mCurrentMusic.source.q320)
                } else {
                    mPlayer.setDataSource(mCurrentMusic.source.q128)
                }
            }
            Qualities.Lossless -> {
                if (!mCurrentMusic.source.lossless.isNullOrEmpty()) {
                    mPlayer.setDataSource(mCurrentMusic.source.lossless)
                    Log.d("Ban dang chay lossless", "Thongtin")
                } else if (!mCurrentMusic.source.q500.isNullOrEmpty()) {
                    mPlayer.setDataSource(mCurrentMusic.source.q500)
                } else if (!mCurrentMusic.source.q320.isNullOrEmpty()) {
                    mPlayer.setDataSource(mCurrentMusic.source.q320)
                } else {
                    mPlayer.setDataSource(mCurrentMusic.source.q128)
                }
            }
        }
        try {
            mPlayer.prepare()
            mPlayer.start()
        } catch (e: IOException) {
            Log.d(e.message, "Loi")
        }


    }

    private fun getSourceCSN(linkCSN: String): Quality {
        var sourceDownload = TaskerExample().execute(linkCSN).get()

        var quality: Quality = Quality()
        var nFill = Pattern.compile("downloadlink2(\\W|\\w)+?center").matcher(sourceDownload)
        if (nFill.find()) {
            var n = Pattern.compile("href=\"((\\W|\\w)+?)\"").matcher(nFill.group(0))
            //Link dang
            //http://chiasenhac.vn/download2.php?v1=1843&v2=1&v3=1YJ2AUA-2XMG2ebG&v4=128&v5=Da%20Lo%20Yeu%20Em%20Nhieu%20-%20JustaTee [128kbps_MP3].mp3

            while (n.find()) {

                when (n.group(1).substring(n.group(1).indexOf('['), n.group(1).indexOf('[') + 4)) {
                    "[128" -> {
                        quality.q128 = n.group(1).substring(0, n.group(1).indexOf("v5=") + 3) + "tenfile" + n.group(1).substring(n.group(1).lastIndexOf('.'), n.group(1).length)
                    }
                    "[320" -> {
                        quality.q320 = n.group(1).substring(0, n.group(1).indexOf("v5=") + 3) + "tenfile" + n.group(1).substring(n.group(1).lastIndexOf('.'), n.group(1).length)
                    }
                    "[500" -> {
                        quality.q500 = n.group(1).substring(0, n.group(1).indexOf("v5=") + 3) + "tenfile" + n.group(1).substring(n.group(1).lastIndexOf('.'), n.group(1).length)
                    }
                    "[Los" -> {
                        quality.lossless = n.group(1).substring(0, n.group(1).indexOf("v5=") + 3) + "tenfile" + n.group(1).substring(n.group(1).lastIndexOf('.'), n.group(1).length)
                    }
                }
            }

        }
        return quality
    }

    fun setAndPlayMusic(music: DataSongFullSimple) {
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
        mPlayer.reset()
    }

    init {
        initSingleton()
    }
}

