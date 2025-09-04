package com.sidnio.siyucloud.ui.phone

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidnio.siyucloud.core.CoreManger
import com.sidnio.siyucloud.utils.error.ErrorCallback
import com.sidnio.siyucloud.utils.extensions.context
import com.sidnio.siyucloud.utils.extensions.errorCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    init {


      //  launchTabViewData()
    }

    fun setContext(context: Context) {
        this.context = context
        launchTabViewData()
    }

    val tabDataList = MutableLiveData<List<TabData>>()

    private fun launchTabViewData() {
        viewModelScope.launch(Dispatchers.IO) {

            val data = CoreManger.network()
                .webdav
                .setUrl("https://192.168.31.40")
                .setRootDirectory("/webdav")
                .setUsername("root")
                .setPassword("123456")
                .setErrorCallback(errorCallback )
                .build()
                .files
                .map { data -> TabData(tabTitle = data.name) }

            tabDataList.postValue(data)
        }
    }


}