package com.example.xon.myapplication.View


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.*
import com.example.xon.myapplication.Adapter.SingletonPlayMusic
import com.example.xon.myapplication.Model.DataSongFullSimple
import com.example.xon.myapplication.Model.InfoDetail
import com.example.xon.myapplication.Model.Qualities
import com.example.xon.myapplication.Model.Quality
import com.example.xon.myapplication.R
import com.example.xon.myapplication.Utils.BlurImage
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.json.JSONObject
import java.util.*
import java.util.regex.Pattern


class FragmentPlayer : Fragment(), View.OnClickListener, SeekBar.OnSeekBarChangeListener {


    lateinit var mViewPlayer: View
    lateinit var mPlayerInfo: InfoDetail
    lateinit var singleton: SingletonPlayMusic
    lateinit var songName: TextView
    lateinit var playPause: ImageButton
    lateinit var quality: TextView
    lateinit var thrumbailplayer: ImageView
    lateinit var seekBar: SeekBar
    lateinit var timer: Timer
    lateinit var layoutPlayer: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mViewPlayer = inflater.inflate(R.layout.fragment_player, container, false)

        initView()

        return mViewPlayer
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.previousplayer -> {
                singleton.prevousMusic()
                songName.text = singleton.mCurrentMusic.songName
            }
            R.id.nextplayer -> {
                singleton.nextMusic()
                songName.text = singleton.mCurrentMusic.songName
            }
            R.id.playpauseplayer -> {
                singleton.pauseMusic()
                songName.text = singleton.mCurrentMusic.songName

                if (singleton.mPlayer.isPlaying) {
                    playPause.setImageResource(R.drawable.ic_pause_white_50dp)
                } else {
                    playPause.setImageResource(R.drawable.ic_play_arrow_white_50dp)
                }
            }
            R.id.qualityplayer -> {
                var menu = PopupMenu(mViewPlayer.context, quality)

                if (!singleton.mCurrentMusic.source.q128.isNullOrEmpty())
                    menu.menu.add(Menu.NONE, 1, Menu.NONE, "128 Kbs")
                if (!singleton.mCurrentMusic.source.q320.isNullOrEmpty())
                    menu.menu.add(Menu.NONE, 1, Menu.NONE, "320 Kbs")
                if (!singleton.mCurrentMusic.source.q500.isNullOrEmpty())
                    menu.menu.add(Menu.NONE, 1, Menu.NONE, "500 Kbs")
                if (!singleton.mCurrentMusic.source.lossless.isNullOrEmpty())
                    menu.menu.add(Menu.NONE, 1, Menu.NONE, "Lossless")
                //inflating popup dung xml file
                //menu.menuInflater.inflate(R.menu.popup_menu,menu.menu)

                //Dang ky listener
                menu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(p0: MenuItem?): Boolean {
                        Toast.makeText(mViewPlayer.context, "You click" + p0!!.title, Toast.LENGTH_LONG).show()
                        if (p0.title == "Lossless") {
                            singleton.defaultQuality = Qualities.Lossless
                            quality.text = "Lossless"
                        } else if (p0.title == "500 Kbs") {
                            quality.text = "500 Kbs"
                            singleton.defaultQuality = Qualities.q500
                        } else if (p0.title == "320 Kbs") {
                            quality.text = "320 Kbs"
                            singleton.defaultQuality = Qualities.q320
                        } else if (p0.title == "128 Kbs") {
                            quality.text = "128 Kbs"
                            singleton.defaultQuality = Qualities.q128
                        }
                        singleton.playMusic()
                        return true
                    }
                })

                menu.show()
            }
        }
    }


    fun getSourceJsonWeb(url: String): String {
        return TaskerExample().execute(url).get()
    }

    private fun initView() {
        val urlMp3: String
        var urlWebCSN: String? = null

        //Chuyển dữ liệu vào player
        urlMp3 = arguments!!.getString("URLMP3", "")
        if (arguments!!.getString("URLWEBCSN") != null) {
            urlWebCSN = arguments!!.getString("URLWEBCSN", null)
        }

        singleton = SingletonPlayMusic.instance


        //Khoi tao cac view
        songName = mViewPlayer.findViewById<TextView>(R.id.songplayer)
        mViewPlayer.findViewById<ImageButton>(R.id.previousplayer).setOnClickListener(this)
        mViewPlayer.findViewById<ImageButton>(R.id.nextplayer).setOnClickListener(this)
        playPause = mViewPlayer.findViewById(R.id.playpauseplayer)
        quality = mViewPlayer.findViewById(R.id.qualityplayer)
        thrumbailplayer = mViewPlayer.findViewById(R.id.thrumbailplayer)
        seekBar = mViewPlayer.findViewById(R.id.seekplayer)
        layoutPlayer = mViewPlayer.findViewById(R.id.layoutplayer)
        timer = Timer()

        //timer.scheduleAtFixedRate(object : TimerTask() {
        //    override fun run() {
        //        seekBar.setProgress(singleton.getCurrentPosition())
        //    }
        //}, 0, 100)


        //seekBar.max = singleton.getDurationMediaPlayer()
        //seekBar.setOnSeekBarChangeListener(this)


        val targetBackground = object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                layoutPlayer.setImageBitmap(BlurImage.fastblur(bitmap, 1f, 50))
            }

            override fun onBitmapFailed(errorDrawable: Drawable) {
                layoutPlayer.setImageResource(R.mipmap.ic_launcher)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable) {

            }
        }

        Picasso.with(mViewPlayer.context)
                .load(singleton.mCurrentMusic.thrumbail)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(targetBackground)



        setQualityText()
        quality.setOnClickListener(this)
        playPause.setOnClickListener(this)

        //Đầu tiên vào kiểm tra thử singleton có chạy ko
        //Nếu nó chạy thì lại kiểm tra cái link đầu vào và cái link bên singleton có trùng không
        //Nếu nó trùng thì mình continua{
        //Nếu nó chạy thì lấy dữ liệu từ thằng current chuyển qua đây
        //}
        //Nếu nó không trùng thì mình sẽ tạo mới
        //Nếu nó không chạy thì tạo mới bằng cách lấy link rồi tạo mới
        //Kiem tra thu thang WebCSN có null không
        //Nếu null thì chỉ lấy source từ bên Mp3.zing thôi
        //Nếu không null thì lấy bên CSN

        if (singleton.mPlayer.isPlaying) {
            if (urlMp3 == singleton.mCurrentMusic.linkMp3) {
                //Thiet lap
                songName.text = singleton.mCurrentMusic.songName
                Picasso.with(mViewPlayer.context).load(singleton.mCurrentMusic.thrumbail).into(thrumbailplayer)

                playPause.setImageResource(R.drawable.ic_pause_white_50dp)
                return
            }
        } else {
            val sourceWeb = getSourceJsonWeb(urlMp3)

            var tokenPattern = Pattern.compile("id=\"zplayerjs-wrapper\"(\\w|\\W)+?key=((\\w|\\W)+?)\"").matcher(sourceWeb)

            var token: String = ""
            if (tokenPattern.find()) {
                token = tokenPattern.group(2)
            }

            val source = getSourceJsonWeb("https://mp3.zing.vn/xhr/media/get-source?type=audio&key=" + token)

            val jsonRoot = JSONObject(source)

            val data = jsonRoot.getJSONObject("data")

            val quality = Quality()

            //Vi khong co https nen them vao thoi
            quality.q128 = "https:" + data.optJSONObject("source").optString("128")

            val player = DataSongFullSimple(
                    data.optString("name"),
                    data.optString("artists_names"),
                    data.optString("link"),
                    null,
                    data.optString("thumbnail"),
                    quality
            )

            //Lay thong tin xong roi Play thoi
            //va chay
            singleton.setAndPlayMusic(player)

            //Thiet lap
            songName.text = singleton.mCurrentMusic.songName
            Picasso.with(mViewPlayer.context).load(singleton.mCurrentMusic.thrumbail).into(thrumbailplayer)

            playPause.setImageResource(R.drawable.ic_pause_white_50dp)

        }


    }

    private fun setQualityText() {
        if (singleton.defaultQuality == Qualities.Lossless) {
            quality.text = "Lossless"
        } else if (singleton.defaultQuality == Qualities.q500) {
            quality.text = "500 Kbs"
        } else if (singleton.defaultQuality == Qualities.q320) {
            quality.text = "320 Kbs"
        } else if (singleton.defaultQuality == Qualities.q128) {
            quality.text = "128 Kbs"
        }
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        singleton.setPosition(p1)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }

}
