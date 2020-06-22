package com.khabar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.khabar.db.ArticleDatabase
import com.khabar.ui.fragments.HomeFragment
import com.khabar.ui.fragments.SavedNewsFragment
import com.khabar.ui.fragments.SearchFragment
import com.khabar.repository.NewsRepository
import com.khabar.ui.NewsViewModel
import com.khabar.ui.NewsViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        setSupportActionBar(app_toolbar) //to setup the app's action bar
        loadFragment(HomeFragment()) //to make home fragment as the default fragment

        bottom_navigation_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_item -> {
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.search_item -> {
                    loadFragment(SearchFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.saved_news_item -> {
                    loadFragment(SavedNewsFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }


    }


    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}