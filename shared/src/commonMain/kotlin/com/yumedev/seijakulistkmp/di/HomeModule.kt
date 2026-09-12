package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.features.home.data.datasource.*
import com.yumedev.seijakulistkmp.features.home.data.repository.*
import com.yumedev.seijakulistkmp.features.home.domain.repository.*
import com.yumedev.seijakulistkmp.features.home.domain.usecase.*
import com.yumedev.seijakulistkmp.features.home.presentation.AnimeHomeViewModel
import com.yumedev.seijakulistkmp.features.home.presentation.MangaHomeViewModel
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.ErrorUiMapper
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

    viewModelOf(::AnimeHomeViewModel)
    viewModelOf(::MangaHomeViewModel)
}
