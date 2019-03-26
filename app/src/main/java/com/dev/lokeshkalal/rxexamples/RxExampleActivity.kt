package com.dev.lokeshkalal.rxexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dev.lokeshkalal.rxexamples.ui.rxexample.RxExampleFragment

class RxExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rx_example_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RxExampleFragment.newInstance())
                .commitNow()
        }
    }

}
