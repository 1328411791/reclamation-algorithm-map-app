package org.liahnu.reclamationAlgorithm.view

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import org.liahnu.reclamationAlgorithm.R
import kotlin.math.log


class WebView : AppCompatActivity() {

    val TAG = "WebView"

    val TARGET_URL = "file:///android_asset/static/index.html"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view)
        // 加载WebView组件

        val webView = findViewById<WebView>(R.id.Webview)

        // 设置在WebView中打开链接，而不是打开浏览器
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && url.startsWith("mailto:")) {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                    return true
                } else if (url != null && url.startsWith("tel:")) {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                    return true
                }
                return false
            }
        }


        val settings = webView.settings;
        // 设置支持JavaScript
        settings.javaScriptEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.domStorageEnabled = true
        settings.databaseEnabled = true

        val originalUserAgent = webView.settings.userAgentString
        settings.userAgentString = "$originalUserAgent Desktop"

        settings.allowUniversalAccessFromFileURLs = true

        // 设置强制横屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE


        Log.i(TAG,"setting success")
        // 加载网页
        webView.loadUrl(TARGET_URL)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i(TAG,"select menu")
        val webView = findViewById<WebView>(R.id.Webview)
        return when (item.itemId) {
            R.id.action_button -> {
                webView.loadUrl(TARGET_URL)
                webView.reload()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_webview,menu)
        return true
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {

        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    private val autoHideHandler = Handler()
    private val hideRunnable = Runnable { hideSystemUI() }
    private val autoHideDelayMillis: Long = 8000  // 设置自动隐藏的延迟时间，这里是3秒


    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)

        // 移除所有的回调和消息
        autoHideHandler.removeCallbacksAndMessages(null)
        // 添加一个新的延迟的Runnable
        autoHideHandler.postDelayed(hideRunnable, autoHideDelayMillis)
    }

}