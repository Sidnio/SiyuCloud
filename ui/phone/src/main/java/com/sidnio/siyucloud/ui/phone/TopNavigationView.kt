package com.sidnio.siyucloud.ui.phone



import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout


class TopNavigationView(mainActivity: Activity, private val tabDataList: List<TabData>) {
    private val context: Context = mainActivity.baseContext
    private val tabLayout = mainActivity.findViewById<TabLayout>(R.id.phone_main_top_navigation_tab_layout)

    fun show() {
        initTabLayout()
    }

    private fun initTabLayout() {
        tabDataList.forEach {
            tabLayout.addTab(setTabTextView(it))
        }

       // tabLayout.tabIndicatorInterpolatorWidth = 70 // 设置选项卡指示器宽度
        tabLayout.tabIndicatorAnimationMode = TabLayout.INDICATOR_ANIMATION_MODE_FADE // 设置选项卡动画模式
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.tabRippleColor = null // 移除选项卡的波纹效果
        tabLayout.setBackgroundColor(Color.TRANSPARENT) //隐藏下划线
        tabLayout.isTabIndicatorFullWidth = false // 设置选项卡指示器是否为全宽

        tabLayout.post {
            setTabPadding(15f) //设置间距
        }

        tabLayout.post { //设置默认选中最后一个
            tabLayout.setScrollPosition(tabLayout.tabCount - 1, 0f, true) // 设置选项卡位置为最后要给
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab) {
                val textView = p0.customView!! as TextView
                val statusBars = tabDataList[p0.position].isLightStatusBars
                if (statusBars) {
                    textView.setTextColor(ContextCompat.getColor(context, com.sidnio.siyucloud.ui.R.color.black))
                } else {
                    textView.setTextColor(ContextCompat.getColor(context, com.sidnio.siyucloud.ui.R.color.white))
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab) {
//                val statusBars = mainDataList[p0.position].isLightStatusBars
//                 if (statusBars) {
//                     updateTabIndicatorBlackColor()
//                } else {
//                    updateTabIndicatorWhiteColor()
//                }
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {}
        })


    }


    /**
     * 实现间距
     */
    private fun setTabPadding(@Suppress("SameParameterValue") p: Float) {

        val dimension =
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                p,
                context.resources.displayMetrics
            )
                .toInt()
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)!!
            val view = tab.view
            view.setPadding(0, 0, 0, 0)

            for (i1 in 0 until view.childCount) {
                val textView = view.getChildAt(i1)
                if (textView is TextView) {
                    view.layoutParams.width = textView.width + dimension
                }
            }
        }
    }

    private fun setTabTextView(tabData: TabData): TabLayout.Tab {


        val string = tabData.tabTitle
        val newTab = tabLayout.newTab()
        val textView = TextView(context)
        textView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        textView.text = string
        textView.textSize = 20f
        textView.setTypeface(Typeface.DEFAULT_BOLD)
        if (tabData.isSelect) {
            textView.setTextColor(ContextCompat.getColor(context, com.sidnio.siyucloud.ui.R.color.orange))
        } else {
            textView.setTextColor(ContextCompat.getColor(context, com.sidnio.siyucloud.ui.R.color.green))
        }

        newTab.customView = textView
        return newTab
    }


    fun getTabLayout(): TabLayout {
        return tabLayout
    }

    fun setTabIndicatorLinear() {
        tabLayout.tabIndicatorAnimationMode = TabLayout.INDICATOR_ANIMATION_MODE_LINEAR
    }

    fun setTabIndicatorFade() {
        tabLayout.tabIndicatorAnimationMode = TabLayout.INDICATOR_ANIMATION_MODE_FADE
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
        setTabPadding(15f)

    }

    fun updateTabIndicatorBlackColor() {
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
        setTabPadding(15f)
    }


    fun getRootLayoutView(): View {
        return tabLayout
    }

}