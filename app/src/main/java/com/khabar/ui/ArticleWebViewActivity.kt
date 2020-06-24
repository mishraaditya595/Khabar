package com.khabar.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.google.android.material.snackbar.Snackbar
import com.khabar.MainActivity
import com.khabar.R
import com.khabar.ui.fragments.ArticleFragmentArgs
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleWebViewActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    val articleFragmentArgs: ArticleFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_web_view)

        viewModel = this.viewModel

        val article = articleFragmentArgs.article

        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        saved_button_fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(findViewById(android.R.id.content), "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }
    }
}