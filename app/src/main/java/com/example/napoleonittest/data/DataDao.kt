package com.example.napoleonittest.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.napoleonittest.models.Banner
import com.example.napoleonittest.models.Offer
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable


@Dao
interface DataDao {




    @Query("SELECT * FROM banners")
    fun getBanners(): Observable<List<Banner>>

    @Query("SELECT * FROM offers")
    fun getOffers(): Observable<List<Offer>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveBanners(list: List<Banner>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveOffers(list: List<Offer>)



    @Query("DELETE FROM banners")
    fun deleteAllBanners()

    @Query("DELETE FROM offers")
    fun deleteAllOffers()

}