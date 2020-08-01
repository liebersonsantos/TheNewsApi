package com.liebersonsantos.thenewsapi.ui.activity.newsactivity

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.liebersonsantos.thenewsapi.R
import com.liebersonsantos.thenewsapi.data.database.ArticleDB
import com.liebersonsantos.thenewsapi.data.repository.NewsRepository
import com.liebersonsantos.thenewsapi.ui.activity.newsactivity.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.coroutines.Dispatchers

class NewsActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val newsRepository = NewsRepository(ArticleDB(this))
        viewModel = NewsViewModel.NewsViewModelProviderFactory(application, newsRepository, Dispatchers.IO).create(NewsViewModel::class.java)

        bottomNavigation.setupWithNavController(newsNavHostFragment.findNavController())
    }

    fun hideKeyboard(){
        val view = this.currentFocus
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}