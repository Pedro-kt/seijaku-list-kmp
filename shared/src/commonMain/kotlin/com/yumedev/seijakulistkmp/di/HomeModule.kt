package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.features.home.data.datasource.FeaturedDataSource
import com.yumedev.seijakulistkmp.features.home.data.datasource.FeaturedDataSourceImpl
import com.yumedev.seijakulistkmp.features.home.data.repository.*
import com.yumedev.seijakulistkmp.features.home.domain.repository.*
import com.yumedev.seijakulistkmp.features.home.domain.usecase.*
import com.yumedev.seijakulistkmp.features.home.presentation.HomeViewModel
import com.yumedev.seijakulistkmp.features.home.presentation.mapper.ErrorUiMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeModule = module {
    singleOf(::ErrorUiMapper)

    singleOf(::FeaturedDataSourceImpl) bind FeaturedDataSource::class
    singleOf(::FeaturedRepositoryImpl) bind FeaturedRepository::class
    factoryOf(::GetFeaturedAnimeUseCase)

    singleOf(::AiringNowRepositoryImpl) bind AiringNowRepository::class
    factoryOf(::GetAiringNowAnimeUseCase)

    singleOf(::NextSeasonRepositoryImpl) bind NextSeasonRepository::class
    factoryOf(::GetNextSeasonAnimeUseCase)

    singleOf(::TopRatedRepositoryImpl) bind TopRatedRepository::class
    factoryOf(::GetTopRatedAnimeUseCase)

    singleOf(::FeaturedMangaRepositoryImpl) bind FeaturedMangaRepository::class
    factoryOf(::GetFeaturedMangaUseCase)
    singleOf(::PublishingMangaRepositoryImpl) bind PublishingMangaRepository::class
    factoryOf(::GetPublishingMangaUseCase)
    singleOf(::PopularMangaRepositoryImpl) bind PopularMangaRepository::class
    factoryOf(::GetPopularMangaUseCase)
    singleOf(::TopRatedMangaRepositoryImpl) bind TopRatedMangaRepository::class
    factoryOf(::GetTopRatedMangaUseCase)
    singleOf(::RecentlyAddedMangaRepositoryImpl) bind RecentlyAddedMangaRepository::class
    factoryOf(::GetRecentlyAddedMangaUseCase)
    singleOf(::ManhwaMangaRepositoryImpl) bind ManhwaMangaRepository::class
    factoryOf(::GetManhwaMangaUseCase)

    viewModelOf(::HomeViewModel)
}
