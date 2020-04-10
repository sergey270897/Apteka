package ru.app.apteka.koin

import org.koin.dsl.module
import ru.app.apteka.repositories.CatalogRepository
import ru.app.apteka.repositories.MedicineRepository

val repositoryModule = module {
    factory { CatalogRepository() }
    factory { MedicineRepository() }
}