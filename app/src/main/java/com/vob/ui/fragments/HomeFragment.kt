package com.vob.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vob.MainActivity
import com.vob.R
import com.vob.adapters.NewsAdapter
import com.vob.ui.ArticleWebViewActivity
import com.vob.ui.NewsViewModel
import com.vob.util.Constants.Companion.QUERY_PAGE_SIZE
import com.vob.util.Resource
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapater: NewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()

        newsAdapater.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            replaceFragment(ArticleFragment(),bundle)
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapater.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 10
                        isLastPage = viewModel.breakingNewsPage == totalPages

                        if (isLastPage)
                        {
                            home_fragment_rv.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "Error: $message ", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> { showProgressBar() }
            }
        })
    }

    var isLoading: Boolean = false
    var isLastPage: Boolean = false
    var isScrolling: Boolean = false

    val scrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling

            if (shouldPaginate){
                viewModel.getBreakingNews("in")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView()
    {
        newsAdapater = NewsAdapter()
        home_fragment_rv.apply {
            adapter = newsAdapater
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HomeFragment.scrollListener)
        }
    }

    private fun hideProgressBar()
    {
        home_pagination_PB.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar()
    {
        home_pagination_PB.visibility = View.VISIBLE
        isLoading = true
    }

    private fun replaceFragment(fragment: Fragment, bundle: Bundle) {
        val transaction = fragmentManager?.beginTransaction()
        val frag= fragment
        frag.arguments = bundle
        transaction?.replace(R.id.container, frag)
        transaction?.addToBackStack(null)?.commit()
    }

    private fun replaceActivity(activity: Activity, bundle: Bundle){
        activity?.let{
            val act = activity
            val intent = Intent (it, ArticleWebViewActivity::class.java)
            it.startActivity(intent)
        }
    }
}