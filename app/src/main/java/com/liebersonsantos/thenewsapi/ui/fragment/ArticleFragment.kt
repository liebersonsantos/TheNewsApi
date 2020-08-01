package com.liebersonsantos.thenewsapi.ui.fragment

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.liebersonsantos.thenewsapi.R
import com.liebersonsantos.thenewsapi.ui.activity.newsactivity.NewsActivity
import com.liebersonsantos.thenewsapi.ui.activity.newsactivity.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()
//    var article: Article? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
//        article = arguments?.getSerializable("article") as Article?   //outra maneira de fazer o getArguments
        val article = args.article

        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Artigo salvo com sucesso", Snackbar.LENGTH_SHORT).show()
        }
    }
}