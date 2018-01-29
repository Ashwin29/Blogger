package com.winision.velammalitapp

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView

class About_Clg : AppCompatActivity() {

    lateinit var webView: WebView
    internal var url = "http://velammalitech.edu.in/Aboutus.html"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about__clg)

            webView = findViewById(R.id.aboutClg) as WebView

            webView.setBackgroundColor(Color.TRANSPARENT)
            webView.settings.loadsImagesAutomatically = true
            webView.settings.javaScriptEnabled = true
            webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

            webView.loadUrl(url)

    }
}
