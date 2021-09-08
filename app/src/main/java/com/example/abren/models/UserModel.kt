package com.example.abren.models

data class UserModel(
    var password:String = "321",
    var phoneNumber:String? = null,
    var emergencyNumber:String? = null,
    var profilePicture:String? = null,
    var idCardPicture:String?=null,
    var idCardBackPicture:String?=null,
    var role:String?=null
)