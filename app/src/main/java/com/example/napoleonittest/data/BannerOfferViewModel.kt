package com.example.napoleonittest.data

import androidx.lifecycle.ViewModel
import com.example.napoleonittest.models.Banner
import com.example.napoleonittest.models.Offer
import io.reactivex.Observable


class BannerOfferViewModel(private val dataSource: DataDao) : ViewModel() {


    fun getBanner(): Observable<List<Banner>> {
        return dataSource.getBanners()
            .map { t: List<Banner> -> t }
    }

    fun getOffer(): Observable<List<Offer>> {
        return dataSource.getOffers()
            .map { t: List<Offer> -> t }
    }


    fun saveOffer(list:List<Offer>){
        dataSource.saveOffers(list)
    }
    fun saveBanner(list:List<Banner>){
        dataSource.saveBanners(list)
    }
}