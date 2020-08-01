package com.liebersonsantos.thenewsapi.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.liebersonsantos.thenewsapi.R
import com.liebersonsantos.thenewsapi.core.Status
import com.liebersonsantos.thenewsapi.pagination.EndlessRecyclerViewListener
import com.liebersonsantos.thenewsapi.ui.activity.newsactivity.NewsActivity
import com.liebersonsantos.thenewsapi.ui.activity.newsactivity.viewmodel.NewsViewModel
import com.liebersonsantos.thenewsapi.ui.adapters.NewsAdapter
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    companion object{
        var TAG = SearchNewsFragment::class.java.simpleName
        const val SEARCH_NEWS_TIME_DELAY = 500L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        var job: Job? = null

        etSearch.addTextChangedListener {editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) viewModel.searchNews(editable.toString())
                }
            }

        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            paginationProgressBar.visibility = if (response.loading == true) View.VISIBLE else View.GONE
            when(response.status){
                Status.SUCCESS -> {
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                        hideKeyboard()
                    }
                }
                Status.ERROR -> {
                    response.errorMessage?.let {
                        Log.d(BreakingNewsFragment.TAG, "Error >> $it")
                    }
                }
                Status.LOADING -> {
                    if (response.loading == true) {
                        paginationProgressBar.visibility = View.VISIBLE
                    } else {
                        paginationProgressBar.visibility = View.GONE
                    }
                }
            }

        })
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter { articleClicked ->
            articleClicked.let {
                val bundle = Bundle().apply {
                    putSerializable("article", articleClicked)
                }

                findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment, bundle)
            }
        }

        with(rvSearchNews){
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = newsAdapter
            addOnScrollListener(object : EndlessRecyclerViewListener(layoutManager as LinearLayoutManager){
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    viewModel.searchNews(etSearch.text.toString())
                }

            })
        }
    }

    private fun showSnackBar(){
        Snackbar.make(rvSearchNews, "Internet currently unavailable", Snackbar.LENGTH_SHORT).show()
    }

    private fun hideKeyboard() {
        (activity as NewsActivity).hideKeyboard()
    }
}