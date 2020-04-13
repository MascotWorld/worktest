package com.example.napoleonittest.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offers")
data class Offer (
    @PrimaryKey
    var id: String,
    var name : String?,
    var groupName: String?,
    var type: String?,
    var image: String?,
    var price: Int?,
    var desc: String?,
    var discount: Float?){

}




/*"id":"2b3b2455-bacb-4ec6-9edb-6682ed99e282",
		"name":"Чиллаут микс (Миндаль-Клюква-Арахис-Кунжут)",
		"groupName":"Прочее",
		"type":"product",
		"image":"https://picsum.photos/240/180/",
		"price":100,
		"discount":0.7*/