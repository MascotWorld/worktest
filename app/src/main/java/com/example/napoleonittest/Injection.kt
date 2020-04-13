package com.example.napoleonittest

import android.content.Context
import com.example.napoleonittest.data.DB
import com.example.napoleonittest.data.DataDao
import com.example.napoleonittest.data.ViewModelFactory

object Injection {

    fun provideUserDataSource(context: Context): DataDao {
        val database = DB.getInstance(context)
        return database.dataDao()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSource = provideUserDataSource(context)
        return ViewModelFactory(dataSource)
    }



}