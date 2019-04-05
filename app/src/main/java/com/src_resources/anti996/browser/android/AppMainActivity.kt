package com.src_resources.anti996.browser.android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebSettings



class AppMainActivity : AppCompatActivity() {

    private lateinit var wvMain: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_main)

        wvMain = findViewById(R.id.wvMain)
        initMainWebView()
        wvMain.loadUrl("https://996.icu")
    }

    /** 初始化控件 wvMain 。 */
    private fun initMainWebView() {
        /*
         * 以下代码节选自 https://www.jianshu.com/p/3c94ae673e2a 。
         */

        //声明WebSettings子类
        val webSettings = wvMain.getSettings()

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        // 注意，开启后可能会发生 XSS 攻击。
        webSettings.setJavaScriptEnabled(true)
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

        //支持插件
//        webSettings.setPluginsEnabled(true)

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true) //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true) // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true) //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false) //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK) //关闭webview中缓存
        webSettings.setAllowFileAccess(true) //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true) //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true) //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8")//设置编码格式
    }
}
