package com.example.napoleonittest.net

import io.reactivex.Observable;
import com.example.napoleonittest.models.Banner
import com.example.napoleonittest.models.Offer
import retrofit2.Call
import retrofit2.http.GET
import java.util.*

interface BaseAPI {



    @GET("banners.json")
    fun getBanners(): Observable<List<Banner>>

    @GET("offers.json")
    fun getOffers(): Observable<List<Offer>>
}