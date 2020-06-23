package com.khabar.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.khabar.MainActivity
import com.khabar.R
import com.khabar.adapters.NewsAdapter
import com.khabar.ui.NewsViewModel
import com.khabar.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.khabar.util.Resource
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapater: NewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view=inflater.inflate(R.layout.fragment_search, container, false)
        
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()

        newsAdapater.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchFragment_to_articleFragment,
                bundle
            )
        }

        var job: Job? = null
        search_ET.addTextChangedListener {editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
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
        search_fragment_rv.apply {
            adapter = newsAdapater
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar()
    {
        search_pagination_PB.visibility = View.INVISIBLE
    }

    private fun showProgressBar()
    {
        search_pagination_PB.visibility = View.VISIBLE
    }


}
