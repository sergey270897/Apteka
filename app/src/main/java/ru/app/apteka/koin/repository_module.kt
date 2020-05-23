package ru.app.apteka.koin

import org.koin.dsl.module
import ru.app.apteka.repositories.AuthRepository
import ru.app.apteka.repositories.CatalogRepository
import ru.app.apteka.repositories.MedicineRepository
import ru.app.apteka.repositories.ProfileRepository
import ru.app.apteka.repositories.TokenRepository

val repositoryModule = module {
    factory { CatalogRepository(get()) }
    factory { MedicineRepository(get(),get(),get())}
    factory { AuthRepository(get()) }
    factory { ProfileRepository(get(), get()) }
    factory { TokenRepository(get()) }
}