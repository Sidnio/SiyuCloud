package com.sidnio.siyucloud.ui.phone

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.sidnio.siyucloud.ui.phone.fragment.VideoFragment

class ContentView(
    private val activity: Activity,
    private val mainList: List<TabData>,
    private val topNavigationView: TopNavigationView
) {


    private val viewPager2: ViewPager2 = activity.findViewById(R.id.phone_main_viewPager2)

    fun show() {
        initContentView()
    }

    private fun initContentView() {

        val tabLayout = topNavigationView.getTabLayout()


        val fragmentList = ArrayList<Fragment>()

        for ((index, mainData) in mainList.withIndex()) {
            fragmentList.add(VideoFragment.newInstance())
        }

        val contentAdapter = ContentAdapter(fragmentList, activity as FragmentActivity)
        viewPager2.adapter = contentAdapter

        var isAnimationMode = false
        viewPager2.setCurrentItem(tabLayout.tabCount, true)

//        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                if (positionOffset != 0f) {
//                    isScroll = true
//                }
//                if (!isAnimationMode) {
//                    isAnimationMode = true
//                    topNavigationView.setTabIndicatorLinear()
//                }
//                tabLayout.setScrollPosition(position, positionOffset, true)
//            }
//
//            override fun onPageSelected(position: Int) {
//                val statusBars = mainList[position].isLightStatusBars
//
//                if (statusBars) {
//                    topNavigationView.updateTabIndicatorBlackColor()
//                    bottomNavigationView.updateTabIndicatorBlackColor()
//                } else {
//                    topNavigationView.updateTabIndicatorWhiteColor()
//                    bottomNavigationView.updateTabIndicatorWhiteColor()
//                }
//
//                tabLayout.selectTab(tabLayout.getTabAt(position))
//
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//                isScroll = false
//                isAnimationMode = false
//                topNavigationView.setTabIndicatorFade()
//            }
//        })

//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                viewPager2.setCurrentItem(tab.position, true)
//            }
//
//            override fun onTabUnselected(p0: TabLayout.Tab?) {
//
//            }
//
//            override fun onTabReselected(p0: TabLayout.Tab?) {
//
//            }
//        })


    }


}