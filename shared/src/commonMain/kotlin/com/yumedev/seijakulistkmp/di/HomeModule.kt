package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.home.data.datasource.*
import com.yumedev.seijakulistkmp.features.home.data.repository.*
import com.yumedev.seijakulistkmp.features.home.domain.model.SectionType
import com.yumedev.seijakulistkmp.features.home.domain.repository.*
import com.yumedev.seijakulistkmp.features.home.domain.usecase.*
import com.yumedev.seijakulistkmp.features.home.presentation.AnimeHomeViewModel
import com.yumedev.seijakulistkmp.features.home.presentation.MangaHomeViewModel
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.ErrorUiMapper
import com.yumedev.seijakulistkmp.features.home.presentation.sectionlist.SectionListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeModule = module {
    singleOf(::ErrorUiMapper)

    singleOf(::AnimeHomeDataSourceImpl) bind AnimeHomeDataSource::class
    factoryOf(::GetAnimeHomeDataUseCase)

    singleOf(::MangaHomeDataSourceImpl) bind MangaHomeDataSource::class
    factoryOf(::GetMangaHomeDataUseCase)

    singleOf(::SectionDataSourceImpl) bind SectionDataSource::class
    factoryOf(::GetSectionMediaUseCase)
    factory { (sectionType: SectionType, mediaType: MediaType) ->
        SectionListViewModel(sectionType, mediaType, get())
    }

    viewModelOf(::AnimeHomeViewModel)
    viewModelOf(::MangaHomeViewModel)
}
