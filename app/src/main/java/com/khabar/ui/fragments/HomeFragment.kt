package com.khabar.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.khabar.MainActivity
import com.khabar.R
import com.khabar.adapters.NewsAdapter
import com.khabar.ui.NewsViewModel
import com.khabar.util.Resource
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
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapater.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "Error: $message ", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> { showProgressBar() }
            }
        })
    }

    private fun setupRecyclerView()
    {
        newsAdapater = NewsAdapter()
        home_fragment_rv.apply {
            adapter = newsAdapater
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar()
    {
        home_pagination_PB.visibility = View.INVISIBLE
    }

    private fun showProgressBar()
    {
        home_pagination_PB.visibility = View.VISIBLE
    }

}