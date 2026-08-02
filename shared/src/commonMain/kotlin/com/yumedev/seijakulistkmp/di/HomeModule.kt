package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.features.home.data.datasource.FeaturedDataSource
import com.yumedev.seijakulistkmp.features.home.data.datasource.FeaturedDataSourceImpl
import com.yumedev.seijakulistkmp.features.home.data.repository.*
import com.yumedev.seijakulistkmp.features.home.domain.repository.*
import com.yumedev.seijakulistkmp.features.home.domain.usecase.*
import com.yumedev.seijakulistkmp.features.home.presentation.HomeViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeModule = module {
    // ========== FEATURED CAROUSEL ==========
    singleOf(::FeaturedDataSourceImpl) bind FeaturedDataSource::class
    singleOf(::FeaturedRepositoryImpl) bind FeaturedRepository::class
    factoryOf(::GetFeaturedAnimeUseCase)

    // ========== AIRING NOW ==========
    singleOf(::AiringNowRepositoryImpl) bind AiringNowRepository::class
    factoryOf(::GetAiringNowAnimeUseCase)

    // ========== NEXT SEASON ==========
    singleOf(::NextSeasonRepositoryImpl) bind NextSeasonRepository::class
    factoryOf(::GetNextSeasonAnimeUseCase)

    // ========== TOP RATED ==========
    singleOf(::TopRatedRepositoryImpl) bind TopRatedRepository::class
    factoryOf(::GetTopRatedAnimeUseCase)

    // ========== PRESENTATION ==========
    viewModelOf(::HomeViewModel)
}
