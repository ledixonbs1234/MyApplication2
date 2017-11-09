package com.example.xon.myapplication

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by XON on 11/8/2017.
 */
class FragmentMain : Fragment {
    lateinit var mviewPager: ViewPager
    lateinit var mTabLayout: TabLayout

    lateinit var viewRoot: View

    constructor()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewRoot = inflater!!.inflate(R.layout.fragment_main, container, false)

        initViewFragment()

        return viewRoot
    }

    private fun initViewFragment() {
        mviewPager = viewRoot.findViewById(R.id.viewpagerid)

        val adapter = AdapterViewPager(fragmentManager)
        adapter.addFragment(OnlineFragment(), "Online")
        adapter.addFragment(OfflineFragment(), "Offline")
        mviewPager.setAdapter(adapter)


        mTabLayout = viewRoot.findViewById(R.id.tabs)

        mTabLayout.setupWithViewPager(mviewPager)
    }
}