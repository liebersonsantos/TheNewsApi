package com.liebersonsantos.thenewsapi.ui.activity.newsactivity.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.*
import com.liebersonsantos.thenewsapi.core.NewsApplication
import com.liebersonsantos.thenewsapi.core.State
import com.liebersonsantos.thenewsapi.data.model.Article
import com.liebersonsantos.thenewsapi.data.model.NewsResponse
import com.liebersonsantos.thenewsapi.data.repository.NewsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsViewModel(application: Application, private val repository: NewsRepository, private val dispatcher: CoroutineDispatcher): AndroidViewModel(application) {
    val breakingNews = MutableLiveData<State<NewsResponse>>()
    var breakingNewsPage = 1

    val searchNews = MutableLiveData<State<NewsResponse>>()
    var searchNewsPage = 1

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(country: String) = viewModelScope.launch {
        try {
            breakingNews.postValue(State.loading(true))
            val response = withContext(dispatcher){
                repository.getBreakingNews(country, breakingNewsPage)
            }
            breakingNews.postValue(State.success(response))

        } catch (throwable: Throwable){
            breakingNews.postValue(State.error(throwable))
        }
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        try {
            searchNews.postValue(State.loading(true))
            val response = withContext(dispatcher){
                repository.searchNews(searchQuery, searchNewsPage)
            }
            searchNews.postValue(State.success(response))

        }catch (throwable: Throwable){
            searchNews.postValue(State.error(throwable))
        }
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.upsertArticle(article)
    }

    fun getSavedNews() = repository.getSavedNews()    //nao precisamos usar viewmodelscope do couroutines tendo em vista q esse metodo retorna um livedata

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    private fun hasInternetConnection(): Boolean{
        val connectionManager = getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectionManager.activeNetwork ?: return false
            val capabilities = connectionManager.getNetworkCapabilities(activeNetwork)?: return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectionManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return false
    }

    class NewsViewModelProviderFactory(private val application: Application, private val repository: NewsRepository, private val dispatcher: CoroutineDispatcher) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
                return NewsViewModel(application, repository, dispatcher) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}