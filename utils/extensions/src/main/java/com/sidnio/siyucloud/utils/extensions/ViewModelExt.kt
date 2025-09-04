package com.sidnio.siyucloud.utils.extensions


import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidnio.siyucloud.utils.common.LongDialog
import com.sidnio.siyucloud.utils.error.ErrorCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ContextPropertyDelegate : ReadWriteProperty<ViewModel, Context?> {
    private var contextRef: WeakReference<Context>? = null

    override fun getValue(thisRef: ViewModel, property: KProperty<*>): Context? {
        return contextRef?.get()
    }

    override fun setValue(thisRef: ViewModel, property: KProperty<*>, value: Context?) {
        contextRef = value?.let { WeakReference(it) }
    }
}

var ViewModel.context: Context? by ContextPropertyDelegate()

val ViewModel.errorCallback: ErrorCallback
    get() = object : ErrorCallback() {
        override fun onError(tag: String, error: Throwable) {
            Log.d(tag, error.message, error)
            viewModelScope.launch(Dispatchers.Main) {
                LongDialog(context!!)
                    .setTag(tag)
                    .setMessage(error.message)
                    .setThrowable(error)
                    .show()
            }


        }
    }