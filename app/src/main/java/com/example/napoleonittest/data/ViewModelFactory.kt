package com.example.napoleonittest.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val dataSource: DataDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BannerOfferViewModel::class.java)) {
            return BannerOfferViewModel(dataSource) as T
        }


        throw IllegalArgumentException("Unknown ViewModel class")
    }
}