package com.sidnio.siyucloud.ui.phone

import android.app.Activity

class MainViewManager {


    private lateinit var tabData: List<TabData>


    fun show(activity: Activity) {

        initTabData()
        val topNavigationView = TopNavigationView(activity, tabData)
        topNavigationView.show()

        val contentView = ContentView(activity, tabData, topNavigationView)
        contentView.show()

    }


    private fun initTabData() {
        tabData = listOf(
            TabData("tst1", com.sidnio.siyucloud.ui.R.color.orange, com.sidnio.siyucloud.ui.R.color.green, true),
            TabData("tst2", com.sidnio.siyucloud.ui.R.color.orange, com.sidnio.siyucloud.ui.R.color.green, true),

            TabData("tst3", com.sidnio.siyucloud.ui.R.color.orange, com.sidnio.siyucloud.ui.R.color.green, true),


            )
    }
}