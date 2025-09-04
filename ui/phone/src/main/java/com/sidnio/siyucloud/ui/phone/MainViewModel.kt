package com.sidnio.siyucloud.ui.phone

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidnio.siyucloud.core.CoreManger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    init {

        launchTabViewData()
    }

      val tabData = MutableLiveData<List<TabData>>()


    private fun launchTabViewData() {
        viewModelScope.launch(Dispatchers.IO) {
            val netWork = CoreManger()
            val webdavBuilder = netWork.network.webdav
            webdavBuilder.setUrl("https://192.168.31.40")
            webdavBuilder.setRootDirectory("/webdav")
            webdavBuilder.setUsername("root")
            webdavBuilder.setPassword("123456")
            val webdav = webdavBuilder.build()

            val tabDataList = webdav.files.map { data ->
                TabData(
                    tabTitle = data.name,
                    contentTextColor = android.R.color.white,
                    contentBackupColor = android.R.color.black,
                    isLightStatusBars = true
                )
            }



            tabData.postValue(tabDataList)


        }
    }


}