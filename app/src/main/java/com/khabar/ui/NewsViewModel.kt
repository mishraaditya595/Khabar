package com.khabar.ui

import androidx.lifecycle.ViewModel
import com.khabar.repository.NewsRepository

class NewsViewModel(val newsRepository: NewsRepository) : ViewModel() {
}