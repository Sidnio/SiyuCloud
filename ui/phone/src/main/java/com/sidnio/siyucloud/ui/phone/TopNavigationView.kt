package com.sidnio.siyucloud.ui.phone


import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.util.TypedValueCompat
import com.google.android.material.tabs.TabLayout

private const val TAG = "TopNavigationView"

class TopNavigationView(mainActivity: Activity, private val tabDataList: List<TabData>) {
    private val context: Context = mainActivity
    private val tabLayout = mainActivity.findViewById<TabLayout>(R.id.phone_main_top_navigation_tab_layout)

    /**
     * 间隔
     */
    private var tabMargin = 0

    /**
     * tab 默认字体大小
     */
    private var tabTextSize = 0f

    /**
     * tab 默认字体大小
     */
    private var tabSelectedTextSize = 0f

    /**
     * 初始当前位置
     */
    private var position = 0

    fun show() {

        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.mainTab, typedValue, true)
        val styleResId = typedValue.resourceId

        context.withStyledAttributes(styleResId, R.styleable.SiyuCloudTheme) {

            tabMargin =  getDimensionPixelSize(R.styleable.SiyuCloudTheme_tabMargin, 20)
            tabTextSize = TypedValueCompat.pxToSp(getDimension(R.styleable.SiyuCloudTheme_tabTextSize, 18f), resources.displayMetrics)
            tabSelectedTextSize = TypedValueCompat.pxToSp(getDimension(R.styleable.SiyuCloudTheme_tabSelectedTextSize, 25f), resources.displayMetrics)
            position = getInt(R.styleable.SiyuCloudTheme_position, 0)

        }


        initTabLayout()
    }

    private fun initTabLayout() {
        tabDataList.forEachIndexed { index, data ->
            addTabTextView(tabLayout, data, index)
        }

        tabLayout.tabIndicatorAnimationMode = TabLayout.INDICATOR_ANIMATION_MODE_FADE // 设置选项卡动画模式
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.tabRippleColor = null // 移除选项卡的波纹效果
        tabLayout.setBackgroundColor(Color.TRANSPARENT) //隐藏下划线
        tabLayout.isTabIndicatorFullWidth = false // 设置选项卡指示器是否为全宽

        tabLayout.post { //设置默认选中某个
            tabLayout.setScrollPosition(0, 0f, true) // 设置选项卡位置为最后要给
        }


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab) {
                Log.d(TAG, "onTabSelected: ${p0.tag}")
                setSelectedTabText(p0, tabSelectedTextSize)
            }

            override fun onTabUnselected(p0: TabLayout.Tab) {
                Log.d(TAG, "onTabUnselected: ${p0.tag}")
                setSelectedTabText(p0, tabTextSize)
            }

            override fun onTabReselected(p0: TabLayout.Tab) { //默认执行第一个选中
                Log.d(TAG, "onTabReselected:  ${p0.tag}")
            }
        })


    }


    fun setTabMargin(tabLayout: TabLayout, margin: Int) {
        tabLayout.post {
            val tabStrip = tabLayout.getChildAt(0) as LinearLayout
            for (i in 0 until tabStrip.childCount) {
                val tabView = tabStrip.getChildAt(i)
                val params = tabView.layoutParams as LinearLayout.LayoutParams
                params.marginStart = margin
                params.marginEnd = margin
                tabView.layoutParams = params
                tabView.invalidate()
            }
        }
    }

    /**
     * 实现间距
     */
    private fun setTabPadding(@Suppress("SameParameterValue") p: Float) {

//        val dimension =
//            TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                p,
//                context.resources.displayMetrics
//            )
//                .toInt()
//        for (i in 0 until tabLayout.tabCount) {
//            val tab = tabLayout.getTabAt(i)!!
//            val view = tab.view
//         //   view.setPadding(0, 0, 0, 0)
//
//            for (i1 in 0 until view.childCount) {
//                val textView = view.getChildAt(i1)
//                if (textView is TextView) {
//                  //  view.layoutParams.width = textView.width + dimension
//                }
//            }
//        }
    }

    /**
     * 添加Tab
     */
    private fun addTabTextView(tabLayout: TabLayout, tabData: TabData, index: Int) {
        val string = tabData.tabTitle

        val newTab = tabLayout.newTab()
        tabLayout.addTab(newTab)
        newTab.tag = index


        newTab.view.post {  // 设置间距
            newTab.view.setPadding(tabMargin, 0, tabMargin, 0)
        }


        val textView = TextView(tabLayout.context)
        textView.text = string

        if (position == index) {
            textView.textSize = tabSelectedTextSize
        } else {
            textView.textSize = tabTextSize
        }
        newTab.customView = textView
    }


    private fun setSelectedTabText(tab: TabLayout.Tab, textSize: Float) {
        val textView = tab.customView as TextView
        textView.textSize = textSize


    }

    fun getTabLayout(): TabLayout {
        return tabLayout
    }

    fun setTabIndicatorLinear() {
      //  tabLayout.tabIndicatorAnimationMode = TabLayout.INDICATOR_ANIMATION_MODE_LINEAR
    }

    fun setTabIndicatorFade() {
     //   tabLayout.tabIndicatorAnimationMode = TabLayout.INDICATOR_ANIMATION_MODE_FADE
    }


    fun updateTabIndicatorWhiteColor() {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)!!
            val view = tab.view

            for (i1 in 0 until view.childCount) {
                val textView = view.getChildAt(i1)
                if (textView is TextView) {
                    textView.setTextColor(ContextCompat.getColor(context, com.sidnio.siyucloud.ui.R.color.orange))
                }
            }
        }

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(context, com.sidnio.siyucloud.ui.R.color.green))
        // setTabPadding(tabMargin)

    }


    /**
     * 更新默认Tab指示器颜色和字体大小
     */
    fun updateTabIndicatorBlackColor() {
//        for (i in 0 until tabLayout.tabCount) {
//            val tab = tabLayout.getTabAt(i)!!
//            val view = tab.view
//
//            for (i1 in 0 until view.childCount) {
//                val textView = view.getChildAt(i1)
//                if (textView is TextView) {
//                    textView.textSize = tabTextSize
//                    textView.setTextColor(ContextCompat.getColor(context, com.sidnio.siyucloud.ui.R.color.orange))
//                }
//            }
//        }
//        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(context, com.sidnio.siyucloud.ui.R.color.green))
//        setTabPadding(tabMargin)
    }


    fun getPosition(): Int {
        return position
    }


}