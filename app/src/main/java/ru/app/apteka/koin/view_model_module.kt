package ru.app.apteka.koin

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.app.apteka.viewmodels.AuthModel
import ru.app.apteka.viewmodels.CartModel
import ru.app.apteka.viewmodels.CatalogModel
import ru.app.apteka.viewmodels.MedicineModel
import ru.app.apteka.viewmodels.ProfileModel

val viewModelModule = module {
    viewModel { CatalogModel(get()) }
    viewModel { (categoryId:Int)-> MedicineModel(categoryId, get(), get()) }
    viewModel { CartModel(get()) }
    viewModel { AuthModel(get(),get()) }
    viewModel { ProfileModel(get()) }
}