package com.khabar.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.khabar.MainActivity
import com.khabar.R
import com.khabar.ui.NewsViewModel
import kotlinx.android.synthetic.main.fragment_article.*


class ArticleFragment : Fragment() {


    lateinit var viewModel: NewsViewModel
    val articleFragmentArgs: ArticleFragmentArgs by navArgs()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        val articleFragmentArgs = articleFragmentArgs.article

        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(articleFragmentArgs.url)
        }
    }

}

