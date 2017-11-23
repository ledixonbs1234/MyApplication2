package com.example.xon.myapplication.View


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.*
import com.example.xon.myapplication.Adapter.SingletonPlayMusic
import com.example.xon.myapplication.Model.DataSongFullSimple
import com.example.xon.myapplication.Model.DataSongManager
import com.example.xon.myapplication.Model.InfoDetail
import com.example.xon.myapplication.Model.Quality
import com.example.xon.myapplication.R
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.util.regex.Pattern


class FragmentPlayer : Fragment(), View.OnClickListener {

    lateinit var mViewPlayer: View
    lateinit var mPlayerInfo: InfoDetail
    lateinit var singleton: SingletonPlayMusic
    lateinit var songName: TextView
    lateinit var playPause: ImageButton
    lateinit var quality: TextView
    lateinit var thrumbailplayer: ImageView

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
                songName.text = singleton.mCurrentMusic.id
            }
            R.id.nextplayer -> {
                singleton.nextMusic()
                songName.text = singleton.mCurrentMusic.id
            }
            R.id.playpauseplayer -> {
                singleton.pauseMusic()
                songName.text = singleton.mCurrentMusic.id

                if (singleton.mPlayer.isPlaying) {
                    playPause.setImageResource(R.drawable.ic_pause_white_50dp)
                } else {
                    playPause.setImageResource(R.drawable.ic_play_arrow_white_50dp)
                }
            }
            R.id.qualityplayer -> {
                var menu = PopupMenu(mViewPlayer.context, quality)

                if (!singleton.mCurrentMusic.mainSong.source.q128.isNullOrEmpty())
                    menu.menu.add(Menu.NONE, 1, Menu.NONE, "128 Kbs")
                if (!singleton.mCurrentMusic.mainSong.source.q320.isNullOrEmpty())
                    menu.menu.add(Menu.NONE, 1, Menu.NONE, "320 Kbs")
                if (!singleton.mCurrentMusic.mainSong.source.q500.isNullOrEmpty())
                    menu.menu.add(Menu.NONE, 1, Menu.NONE, "500 Kbs")
                if (!singleton.mCurrentMusic.mainSong.source.lossless.isNullOrEmpty())
                    menu.menu.add(Menu.NONE, 1, Menu.NONE, "Lossless")
                //inflating popup dung xml file
                //menu.menuInflater.inflate(R.menu.popup_menu,menu.menu)

                //Dang ky listener
                menu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(p0: MenuItem?): Boolean {
                        Toast.makeText(mViewPlayer.context, "You click" + p0!!.title, Toast.LENGTH_LONG).show()
                        if (p0.title == "Lossless") {
                            singleton.defaultQuality = "lossless"
                        } else if (p0.title == "500 Kbs") {
                            singleton.defaultQuality = "500"
                        } else if (p0.title == "320 Kbs") {
                            singleton.defaultQuality = "320"
                        } else if (p0.title == "128 Kbs") {
                            singleton.defaultQuality = "128"
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
        var url: String = ""
        if (arguments != null) {
            url = arguments!!.getString("URL", "")
        }

        //Khoi tao cac view
        songName = mViewPlayer.findViewById<TextView>(R.id.songplayer)
        mViewPlayer.findViewById<ImageButton>(R.id.previousplayer).setOnClickListener(this)
        mViewPlayer.findViewById<ImageButton>(R.id.nextplayer).setOnClickListener(this)
        playPause = mViewPlayer.findViewById(R.id.playpauseplayer)
        quality = mViewPlayer.findViewById(R.id.qualityplayer)
        thrumbailplayer = mViewPlayer.findViewById(R.id.thrumbailplayer)
        quality.setOnClickListener(this)
        playPause.setOnClickListener(this)


        singleton = SingletonPlayMusic.instance

        //Neu co url thì làm mới trang
        //Neu ko co thì tạo coi thử singleton có chạy ko nếu ko bó tay
        if (!url.isNullOrEmpty()) {

            val sourceWeb = getSourceJsonWeb(url)

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
            quality.q320 = data.optJSONObject("source").optString("320")

            val player = DataSongFullSimple(
                    data.optString("name"),
                    data.optString("artists_names"),
                    data.optString("link"),
                    data.optString("thumbnail"),
                    quality
            )

            //Lay thong tin xong roi Play thoi


            //va chay
            singleton.setAndPlayMusic(DataSongManager(player.songName, player, null))

        }
        //Thiet lap
        songName.text = singleton.mCurrentMusic.id
        Picasso.with(mViewPlayer.context).load(singleton.mCurrentMusic.mainSong.thrumbail).into(thrumbailplayer)
        if (singleton.mPlayer.isPlaying) {
            playPause.setImageResource(R.drawable.ic_pause_white_50dp)
        } else {
            playPause.setImageResource(R.drawable.ic_play_arrow_white_50dp)
        }


    }


}
