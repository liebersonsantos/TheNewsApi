package com.liebersonsantos.thenewsapi.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
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
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment: Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    companion object{
        val TAG = BreakingNewsFragment::class.java.simpleName
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            paginationProgressBar.visibility = if (response.loading == true) View.VISIBLE else View.GONE
            when(response.status){
                Status.SUCCESS -> {
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                    }
                }

                Status.ERROR -> {
                    response.errorMessage?.let {
                        Log.d(TAG, "Error >> $it")
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
            val bundle = Bundle().apply {
                putSerializable("article", articleClicked)
            }

            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment, bundle)
        }

        recyclerViewBreakingNews.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = newsAdapter
            addOnScrollListener(object : EndlessRecyclerViewListener(layoutManager as LinearLayoutManager){
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    viewModel.getBreakingNews("us")
                }

            })
        }

//        with(recyclerViewBreakingNews){
//            layoutManager = LinearLayoutManager(activity)
//            setHasFixedSize(true)
//            adapter = newsAdapter
//            addOnScrollListener(object : EndlessRecyclerViewListener(layoutManager as LinearLayoutManager){
//                override fun onLoadMore(page: Int, totalItemsCount: Int) {
//                    viewModel.getBreakingNews("us",  page)
//                }
//
//            })
//        }
    }

    private fun showSnackBar(){
        Snackbar.make(recyclerViewBreakingNews, "Internet currently unavailable", Snackbar.LENGTH_SHORT).show()
    }

}
