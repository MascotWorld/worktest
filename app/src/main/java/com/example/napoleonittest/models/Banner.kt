package com.example.napoleonittest.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "banners")
data class Banner(
    @PrimaryKey
    val id: String,
    val title: String?,
    val desc: String?,
    val image: String?
)