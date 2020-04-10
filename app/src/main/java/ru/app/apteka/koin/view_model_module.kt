package ru.app.apteka.koin

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.app.apteka.viewmodels.CatalogModel
import ru.app.apteka.viewmodels.MedicineModel

val viewModelModule = module {
    viewModel { CatalogModel(get()) }
    viewModel { (categoryId:Int)-> MedicineModel(categoryId, get()) }
}