package com.example.xon.myapplication.View

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xon.myapplication.Adapter.AdapterViewPager
import com.example.xon.myapplication.R

/**
 *
 * Created by XON on 11/8/2017.
 */
class FragmentMain : Fragment() {
    lateinit var mviewPager: ViewPager
    lateinit var mTabLayout: TabLayout
    lateinit var mFragmentOnline: Fragment
    lateinit var mFragmentOffline: Fragment
    lateinit var mViewRoot: Activity
    lateinit var viewMain: View

    var mBool: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewMain = inflater.inflate(R.layout.fragment_main, container, false)

        initViewFragment()


        return viewMain
    }

    private fun initViewFragment() {
        mviewPager = viewMain.findViewById(R.id.viewpagerid)

        val adapter = AdapterViewPager(childFragmentManager)

        mFragmentOffline = OfflineFragment()
        mFragmentOnline = OnlineFragment()

        adapter.addFragment(OnlineFragment(), "Online")
        adapter.addFragment(OfflineFragment(), "Offline")
        mviewPager.adapter = adapter


        mTabLayout = viewMain.findViewById(R.id.tabs)

        mTabLayout.setupWithViewPager(mviewPager)
    }

}