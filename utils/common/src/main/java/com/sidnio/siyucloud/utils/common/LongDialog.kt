package com.sidnio.siyucloud.utils.common

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.PrintWriter
import java.io.StringWriter


class LongDialog(context: Context) {

    private val dialog = MaterialAlertDialogBuilder(context)

    private var tag: String = ""
    private var message: String? = ""
    private var printStackTrace: String? = ""

    fun setTag(tag: String): LongDialog {
        this.tag = "$tag:"
        return this
    }

    fun setMessage(message: String?): LongDialog {
        this.message = message
        return this
    }

    fun setThrowable(throwable: Throwable): LongDialog {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        throwable.printStackTrace(pw)
        val stackTrace: String? = sw.toString()
        printStackTrace = "\n" + stackTrace
        return this
    }

    fun show() {
        dialog.setMessage("$tag$message$printStackTrace")
        // dialog.setNegativeButton("拒绝", null)
        dialog.setPositiveButton("返回", null) // TODO 字符串
        dialog.show()
    }
}