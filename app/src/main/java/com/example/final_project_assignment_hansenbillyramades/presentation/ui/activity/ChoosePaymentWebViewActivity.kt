package com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.final_project_assignment_hansenbillyramades.databinding.ActivityChoosePaymentWebViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChoosePaymentWebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChoosePaymentWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoosePaymentWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarChoosePayment)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val paymentUrl = intent.getStringExtra("paymentUrl")

        if (paymentUrl != null) {
            binding.wvPayment.loadUrl(paymentUrl)
        } else {
            Toast.makeText(this, "Invalid payment URL", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.wvPayment.settings.javaScriptEnabled = true

        binding.wvPayment.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.pbLoadingWebView.isVisible = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.pbLoadingWebView.isVisible = false
            }


            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?,
            ) {
                super.onReceivedError(view, request, error)
                Toast.makeText(
                    this@ChoosePaymentWebViewActivity,
                    "Please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.wvPayment.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.pbLoadingWebView.progress = newProgress
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}