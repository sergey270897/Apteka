package ru.app.apteka.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.app.apteka.viewmodels.MedicineModel

class MedicineModelFactory(val categoryId:Int): ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MedicineModel(categoryId) as T
    }
}