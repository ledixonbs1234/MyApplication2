package com.example.xon.myapplication.View


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xon.myapplication.Adapter.AdapterViewType
import com.example.xon.myapplication.Model.DataWebModel

import com.example.xon.myapplication.R
import java.util.regex.Pattern


class FragmentType : Fragment() {

    lateinit var mViewRoot: View
    lateinit var recView: RecyclerView
    lateinit var mAdapter: AdapterViewType
    var mItems = arrayListOf<DataWebModel>()
    lateinit var mContextAc: Context


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mViewRoot = inflater.inflate(R.layout.fragment_type, container, false)
        // Inflate the layout for this fragment
        return mViewRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getSourceTop()
        initView()


    }

    fun getSourceJsonWeb(url: String): String {
        return TaskerExample().execute(url).get()
    }


    private fun getSourceTop() {
        var source = getSourceJsonWeb("https://mp3.zing.vn/chu-de")
        var n = Pattern.compile("part-cate\"(\\W|\\w)+?footer").matcher(source)
        var sourceFill: String = ""
        if (n.find()) {
            sourceFill = n.group(0)
        }
        var m = Pattern.compile("<a href=\"((\\w|\\W)+?)\" title=\"((\\w|\\W)+?)\"(\\w|\\W)+?src=\"((\\w|\\W)+?)\"").matcher(sourceFill)
        while (m.find()) {
            mItems.add(DataWebModel(m.group(6), "https://mp3.zing.vn" + m.group(1), m.group(3)))
        }
    }

    private fun initView() {
        mAdapter = AdapterViewType(mItems, mContextAc)
        recView = mViewRoot.findViewById(R.id.rectype)
        recView.adapter = mAdapter
        //var layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)
        //recView.layoutManager = layoutManager
        var layoutManager = GridLayoutManager(this.context, 2)
        recView.layoutManager = layoutManager
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mContextAc = context!!
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement onViewd")
        }
    }
}
