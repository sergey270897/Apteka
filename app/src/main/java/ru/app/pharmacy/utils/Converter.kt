package ru.app.pharmacy.utils

import androidx.lifecycle.MutableLiveData
import androidx.room.TypeConverter

class Converter {
    @TypeConverter
    fun liveData2Int(data: MutableLiveData<Int>): Int = data.value!!

    @TypeConverter
    fun int2LiveData(data: Int): MutableLiveData<Int> = MutableLiveData(data)
}