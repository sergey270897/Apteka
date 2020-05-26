package ru.app.pharmacy.koin

import org.koin.dsl.module
import ru.app.pharmacy.repositories.AuthRepository
import ru.app.pharmacy.repositories.CatalogRepository
import ru.app.pharmacy.repositories.MedicineRepository
import ru.app.pharmacy.repositories.OrderRepository
import ru.app.pharmacy.repositories.ProfileRepository
import ru.app.pharmacy.repositories.TokenRepository

val repositoryModule = module {
    factory { CatalogRepository(get()) }
    factory { MedicineRepository(get(),get(),get())}
    factory { AuthRepository(get()) }
    factory { ProfileRepository(get(), get()) }
    factory { TokenRepository(get()) }
    factory { OrderRepository(get()) }
}