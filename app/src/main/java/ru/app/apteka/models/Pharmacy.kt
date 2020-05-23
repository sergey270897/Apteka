package ru.app.apteka.models

data class PharmacyResponse(val pharmacy:List<Pharmacy>)

data class Pharmacy(val id:Int, val name:String, val address:String)