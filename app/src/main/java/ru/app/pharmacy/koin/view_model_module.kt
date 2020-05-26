package ru.app.pharmacy.koin

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.app.pharmacy.viewmodels.AuthModel
import ru.app.pharmacy.viewmodels.CartModel
import ru.app.pharmacy.viewmodels.CatalogModel
import ru.app.pharmacy.viewmodels.MedicineModel
import ru.app.pharmacy.viewmodels.OrderModel
import ru.app.pharmacy.viewmodels.ProfileModel

val viewModelModule = module {
    viewModel { CatalogModel(get()) }
    viewModel { (categoryId:Int)-> MedicineModel(categoryId, get(), get()) }
    viewModel { CartModel(get()) }
    viewModel { AuthModel(get(),get()) }
    viewModel { ProfileModel(get()) }
    viewModel { OrderModel(get()) }
}