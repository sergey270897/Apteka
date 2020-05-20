package ru.app.apteka.models

data class Profile(var token:String?, var refresh:String?, var name:String?, var email:String?){
    var city:String? = null
    fun getInitials():String = "S"//name?.first()?.toUpperCase().toString()
}

data class EmailResponse(val email:String, val error:Int)

data class AuthResponse(val accessToken:String, val refreshToken:String)