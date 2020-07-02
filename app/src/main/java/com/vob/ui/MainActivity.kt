package com.vob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.vob.db.ArticleDatabase
import com.vob.ui.fragments.HomeFragment
import com.vob.ui.fragments.SavedNewsFragment
import com.vob.ui.fragments.SearchFragment
import com.vob.repository.NewsRepository
import com.vob.ui.NewsViewModel
import com.vob.ui.NewsViewModelProviderFactory
import com.vob.ui.SettingsActivity
import com.vob.ui.fragments.LocationBasedNewsFraagment
import com.vob.util.Constants.Companion.ADMOB_APP_ID
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    private lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdView = findViewById(R.id.adView)

        MobileAds.initialize(this, ADMOB_APP_ID)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        setSupportActionBar(app_toolbar) //to setup the app's action bar
        loadFragment(HomeFragment()) //to make home fragment as the default fragment

        bottom_navigation_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_item -> {
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.location_based_news_item -> {
                    loadFragment(LocationBasedNewsFraagment())
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }
        return true
    }
}