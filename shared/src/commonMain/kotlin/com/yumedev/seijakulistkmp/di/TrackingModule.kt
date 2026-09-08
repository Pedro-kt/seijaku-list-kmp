package com.yumedev.seijakulistkmp.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.yumedev.seijakulistkmp.features.profile.data.repository.ProfileRepositoryImpl
import com.yumedev.seijakulistkmp.features.tracking.data.local.migrations.MIGRATION_2_3
import com.yumedev.seijakulistkmp.features.profile.domain.repository.ProfileRepository
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.EnsureLocalProfileExistsUseCase
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.GetCurrentProfileUseCase
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.UpdateProfileStatisticsUseCase
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.UpdateProfileUseCase
import com.yumedev.seijakulistkmp.features.profile.presentation.ProfileViewModel
import com.yumedev.seijakulistkmp.features.tracking.data.export.MALXmlMapper
import com.yumedev.seijakulistkmp.features.tracking.data.local.TrackingDatabase
import com.yumedev.seijakulistkmp.features.tracking.data.local.TrackingDatabaseBuilder
import com.yumedev.seijakulistkmp.features.tracking.data.repository.MediaListRepositoryImpl
import com.yumedev.seijakulistkmp.features.tracking.domain.repository.MediaListRepository
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.AddToListUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.CheckInListUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.ExportToMALUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.GetListEntryUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.GetListStatsUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.GetMediaListUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.ImportFromMALUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.RemoveFromListUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.ResolveAllImportConflictsUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.ResolveImportConflictUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.UpdateListEntryUseCase
import com.yumedev.seijakulistkmp.features.tracking.presentation.animelist.UserAnimeListViewModel
import com.yumedev.seijakulistkmp.features.tracking.presentation.mangalist.UserMangaListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val trackingModule = module {
    single<TrackingDatabase> {
        TrackingDatabaseBuilder.create()
            .setDriver(BundledSQLiteDriver())
            .addMigrations(MIGRATION_2_3)
            .build()
    }

    single { get<TrackingDatabase>().mediaListDao() }
    single { get<TrackingDatabase>().userProfileDao() }

    singleOf(::MALXmlMapper)
    singleOf(::MediaListRepositoryImpl) bind MediaListRepository::class
    singleOf(::ProfileRepositoryImpl) bind ProfileRepository::class

    factoryOf(::GetCurrentProfileUseCase)
    factoryOf(::UpdateProfileUseCase)
    factoryOf(::UpdateProfileStatisticsUseCase)
    factoryOf(::EnsureLocalProfileExistsUseCase)

    factoryOf(::AddToListUseCase)
    factoryOf(::UpdateListEntryUseCase)
    factoryOf(::RemoveFromListUseCase)
    factoryOf(::GetMediaListUseCase)
    factoryOf(::GetListEntryUseCase)
    factoryOf(::GetListStatsUseCase)
    factoryOf(::ExportToMALUseCase)
    factoryOf(::ImportFromMALUseCase)
    factoryOf(::CheckInListUseCase)
    factoryOf(::ResolveImportConflictUseCase)
    factoryOf(::ResolveAllImportConflictsUseCase)

    viewModelOf(::ProfileViewModel)
    viewModelOf(::UserAnimeListViewModel)
    viewModelOf(::UserMangaListViewModel)
}
