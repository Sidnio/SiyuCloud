package com.sidnio.siyucloud.ui.phone

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.phone_activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
      viewModel.setContext(this)

        val builder = MainViewManager.Builder()
        Log.d(TAG, "onCreate: $this")

        viewModel.tabDataList.observe( this){
            it.forEach { tabData ->
                Log.d(TAG, "onCreate: ${tabData.tabTitle}")
            }
            builder.addTabData(it)
            builder.show( this)
        }

    }
}