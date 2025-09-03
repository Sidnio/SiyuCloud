package com.sidnio.siyucloud.ui.phone

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.sidnio.siyucloud.core.CoreManger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.phone_activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val mainViewManager = MainViewManager()
        mainViewManager.show(this)






        lifecycleScope.launch(Dispatchers.IO) {
            val netWork = CoreManger()
            val webdavBuilder = netWork.network.webdav
            webdavBuilder.setUrl("https://192.168.31.40/webdav")
            webdavBuilder.setUsername("root")
            webdavBuilder.setPassword("123456")
            webdavBuilder.build().request()
        }
    }
}