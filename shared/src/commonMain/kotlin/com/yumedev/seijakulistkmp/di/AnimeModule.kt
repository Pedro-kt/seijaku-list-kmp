package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.features.anime.data.repository.AnimeRepositoryImpl
import com.yumedev.seijakulistkmp.features.anime.domain.repository.AnimeRepository
import com.yumedev.seijakulistkmp.features.anime.domain.usecase.GetTrendingAnimeUseCase
import com.yumedev.seijakulistkmp.features.anime.domain.usecase.SearchAnimeUseCase
import com.yumedev.seijakulistkmp.features.anime.presentation.list.AnimeListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Dependency injection module for Anime feature
 */
val animeModule = module {
    // Repository
    singleOf(::AnimeRepositoryImpl) bind AnimeRepository::class

    // Domain Layer - Use Cases
    factoryOf(::GetTrendingAnimeUseCase)
    factoryOf(::SearchAnimeUseCase)

    // Presentation Layer - ViewModels
    viewModelOf(::AnimeListViewModel)
}
