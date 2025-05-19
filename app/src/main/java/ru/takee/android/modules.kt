package ru.takee.android

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.takee.android.cv.CVManager
import ru.takee.android.db.TakeeDatabase

fun App.createStatisticsModule() = module(createdAtStart = true) {

    single {
        TakeeDatabase.getInstance(androidContext())
    }

    single {
        get<TakeeDatabase>().petDao
    }

    single {
        CVManager(androidContext())
    }

}