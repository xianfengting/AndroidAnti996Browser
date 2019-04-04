package com.src_resources.anti996.browser.android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class AppMainActivity : AppCompatActivity() {

    private lateinit var mainWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_main)

        mainWebView = findViewById(R.id.mainWebView)
        mainWebView.loadUrl("https://996.icu")
    }
}
