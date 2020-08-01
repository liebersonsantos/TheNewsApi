package com.liebersonsantos.thenewsapi.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.liebersonsantos.thenewsapi.R
import com.liebersonsantos.thenewsapi.data.model.Article
import com.liebersonsantos.thenewsapi.ui.activity.newsactivity.NewsActivity
import com.liebersonsantos.thenewsapi.ui.activity.newsactivity.viewmodel.NewsViewModel
import com.liebersonsantos.thenewsapi.ui.adapters.NewsAdapter
import kotlinx.android.synthetic.main.fragment_saved_news.*

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()
        deleteArticle(view)

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { savedArticles ->
            savedArticles?.let {
                newsAdapter.differ.submitList(it)
            }
        })
    }

    private fun deleteArticle(view: View) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val article = newsAdapter.differ.currentList[viewHolder.adapterPosition]
                viewModel.deleteArticle(article)
                showSnackBar(view, article)
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedNews)
        }
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter { articleClicked ->
            val bundle = Bundle().apply {
                putSerializable("article", articleClicked)
            }

            findNavController().navigate(R.id.action_savedNewsFragment_to_articleFragment, bundle)
        }

        with(rvSavedNews){
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = newsAdapter
        }
    }

    private fun showSnackBar(view: View, article: Article){
        Snackbar.make(view, "Artigo deletado com sucesso!", Snackbar.LENGTH_LONG).apply {
            setAction("Desfazer"){
                viewModel.saveArticle(article)
            }
            show()
        }
    }
}