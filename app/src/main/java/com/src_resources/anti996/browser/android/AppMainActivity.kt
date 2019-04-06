package com.src_resources.anti996.browser.android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AlertDialog
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import com.blankj.utilcode.util.LogUtils


class AppMainActivity : AppCompatActivity() {

    companion object {
        /**
         * Handler 标识 - 更新加载进度
         * 参数说明：
         * arg1 - 要更新的进度。
         */
        const val HANDLER_FLAG_UPDATE_LOADING_PROGRESS = 0
    }

    inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                // Handler 标识 - 更新加载进度
                HANDLER_FLAG_UPDATE_LOADING_PROGRESS -> {
                    var progress = msg.arg1
                    if (progress >= pbLoadingProgress.max) {
                        progress = pbLoadingProgress.max
                        pbLoadingProgress.visibility = View.INVISIBLE
                    } else {
                        pbLoadingProgress.visibility = View.VISIBLE
                    }
                    pbLoadingProgress.progress = progress
                }
            }
        }
    }

    private lateinit var pbLoadingProgress: ProgressBar
    private lateinit var wvMain: WebView

    private lateinit var mHandler: MyHandler
    private var mWebpageTitle = ""
            set(value) {
                field = value
                // 设置 Activity 的标题为网页的标题。
                title = value
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_main)

        mHandler = MyHandler()

        pbLoadingProgress = findViewById(R.id.pbLoadingProgress)
        // 使控件 pbLoadingProgress 位于最顶层。
        pbLoadingProgress.bringToFront()

        wvMain = findViewById(R.id.wvMain)
        initMainWebView()
//        wvMain.loadUrl("https://996.icu")
//        wvMain.loadUrl("file:////android_asset/mainPage-zh_CN.html")
        showWelcomeDialog()
    }

    override fun onBackPressed() {
        if (wvMain.canGoBack()) {
            wvMain.goBack()
        } else {
            super.onBackPressed()
        }
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

        /*
         * 解决WebView加载URL跳转到系统浏览器的问题
         * 引用自 https://blog.csdn.net/yy1300326388/article/details/43965493
         */
        /*
        // 以下是 Java 版的代码：
        wvMain.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
         */
        wvMain.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                view?.loadUrl(request?.url?.toString())
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        wvMain.webChromeClient = object : WebChromeClient() {
            /**
             * 实现网页加载进度的监听。
             * 参考：https://blog.csdn.net/u010319687/article/details/50207233
             */
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                // 向 Handler 发送消息以更新进度。
                val msg = mHandler.obtainMessage()
                msg.what = HANDLER_FLAG_UPDATE_LOADING_PROGRESS
                msg.arg1 = newProgress
                mHandler.sendMessage(msg)
            }

            /**
             * 获取网页标题。
             */
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                mWebpageTitle = title ?: ""
            }
        }
    }

    /** 显示欢迎对话框。 */
    private fun showWelcomeDialog() {
        val welcomeDialog = AlertDialog.Builder(this)
                .setTitle(R.string.welcomeToUse)
                // 加了下面这一行会导致选项被隐藏。
//                .setMessage(R.string.dialog_selectAUrlToBrowse)
                .setItems(R.array._996UrlsDescription) { dialog, which ->
                    val url = resources.getStringArray(R.array._996Urls)[which]
                    LogUtils.d("which=$which", "url=$url")
                    wvMain.loadUrl(url)
                }
                .create()
        welcomeDialog.show()
    }
}
