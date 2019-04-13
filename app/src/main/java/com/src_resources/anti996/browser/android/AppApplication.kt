package com.src_resources.anti996.browser.android

import android.app.Application
import com.blankj.utilcode.util.FileUtils
import java.io.File

/**
 * The Application class.
 */
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 清除浏览器缓存。
        clearBrowserCache()
    }

    /** 清除浏览器缓存。 */
    private fun clearBrowserCache() {
        var browserCacheFolder = File(cacheDir, "org.chromium.android_webview")
        if (browserCacheFolder.exists() && browserCacheFolder.isDirectory)
            FileUtils.deleteDir(browserCacheFolder)
    }
}
