package com.example.xon.myapplication.View


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xon.myapplication.Adapter.AdapterViewTypeDetail
import com.example.xon.myapplication.Model.DataWebModel

import com.example.xon.myapplication.R
import java.util.regex.Pattern

class FragmentDetailType : Fragment {

    lateinit var mViewRoot: View
    lateinit var recView: RecyclerView
    lateinit var mAdapter: AdapterViewTypeDetail
    var mItems = arrayListOf<DataWebModel>()
    lateinit var mTypeUrl: String
    lateinit var mContextAc: Context


    constructor() : super()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mContextAc = context!!
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "class just implement to View")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mViewRoot = inflater.inflate(R.layout.fragment_detail_type, container, false)
        // Inflate the layout for this fragment
        return mViewRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTypeUrl = arguments!!.get("URLType").toString()
        getSourceTop()
        initView()
    }

    fun getSourceJsonWeb(url: String): String {
        return TaskerExample().execute(url).get()
    }


    private fun getSourceTop() {
        var source = getSourceJsonWeb(mTypeUrl)
        var n = Pattern.compile("album-item(\\W|\\w)+?href=\"((\\w|\\W)+?)\"(\\w|\\W)+?src=\"((\\w|\\W)+?)\"(\\w|\\W)+?alt=\"((\\w|\\W)+?)\"").matcher(source)

        while (n.find()) {
            mItems.add(DataWebModel(n.group(5), "https://mp3.zing.vn" + n.group(2), n.group(8)))
        }
    }

    private fun initView() {
        mAdapter = AdapterViewTypeDetail(mItems, mContextAc)
        recView = mViewRoot.findViewById(R.id.rectypedetail)
        recView.adapter = mAdapter
        //var layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)
        //recView.layoutManager = layoutManager
        var layoutManager = GridLayoutManager(this.context, 2)
        recView.layoutManager = layoutManager
    }


}
