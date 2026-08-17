package com.yumedev.seijakulistkmp.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.yumedev.seijakulistkmp.features.tracking.data.export.MALXmlMapper
import com.yumedev.seijakulistkmp.features.tracking.data.local.TrackingDatabase
import com.yumedev.seijakulistkmp.features.tracking.data.local.TrackingDatabaseBuilder
import com.yumedev.seijakulistkmp.features.tracking.data.repository.MediaListRepositoryImpl
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.AddToListUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.CheckInListUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.ExportToMALUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.GetListStatsUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.GetMediaListUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.ImportFromMALUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.RemoveFromListUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.UpdateListEntryUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val trackingModule = module {
    single<TrackingDatabase> {
        TrackingDatabaseBuilder.create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single { get<TrackingDatabase>().mediaListDao() }

    singleOf(::MALXmlMapper)
    singleOf(::MediaListRepositoryImpl) bind MediaListRepository::class

    factoryOf(::AddToListUseCase)
    factoryOf(::UpdateListEntryUseCase)
    factoryOf(::RemoveFromListUseCase)
    factoryOf(::GetMediaListUseCase)
    factoryOf(::GetListStatsUseCase)
    factoryOf(::ExportToMALUseCase)
    factoryOf(::ImportFromMALUseCase)
    factoryOf(::CheckInListUseCase)

    // Presentation Layer - ViewModels will be added later
    // viewModelOf(::AnimeListViewModel)
    // viewModelOf(::MangaListViewModel)
}
