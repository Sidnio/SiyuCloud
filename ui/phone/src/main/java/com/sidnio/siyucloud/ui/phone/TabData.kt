package com.sidnio.siyucloud.ui.phone

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

data class TabData(
    val tabTitle: String,
    @ColorRes val contentTextColor: Int,
    @ColorRes val contentBackupColor: Int,
    val isLightStatusBars: Boolean,
    var isSelect: Boolean = false //默认选中
)
