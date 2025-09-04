package com.sidnio.siyucloud.ui.phone

import android.app.Activity

class MainViewManager {


    class Builder {
        private lateinit var tabData: List<TabData>

        fun addTabData(tabData: List<TabData>): Builder {
            this.tabData =tabData
            return this
        }

        fun show(activity: Activity)  {
            val topNavigationView = TopNavigationView(activity, tabData)
            topNavigationView.show()

            val contentView = ContentView(activity, tabData, topNavigationView)
            contentView.show()
        }
    }





}