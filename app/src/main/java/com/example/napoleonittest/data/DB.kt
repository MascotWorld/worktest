package com.example.napoleonittest.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.napoleonittest.models.Banner
import com.example.napoleonittest.models.Offer
import androidx.room.Database



@Database(entities = [Banner::class, Offer::class], version = 1)
abstract class DB : RoomDatabase() {

    abstract fun dataDao(): DataDao

    companion object {

        @Volatile private var INSTANCE: DB? = null

        fun getInstance(context: Context): DB =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                DB::class.java, "Sample.db")
                .build()
    }
}