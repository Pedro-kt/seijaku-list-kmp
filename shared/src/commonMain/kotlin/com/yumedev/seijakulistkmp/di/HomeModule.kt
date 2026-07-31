package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.features.home.data.datasource.FeaturedDataSource
import com.yumedev.seijakulistkmp.features.home.data.datasource.FeaturedDataSourceImpl
import com.yumedev.seijakulistkmp.features.home.data.repository.FeaturedRepository
import com.yumedev.seijakulistkmp.features.home.data.repository.FeaturedRepositoryImpl
import com.yumedev.seijakulistkmp.features.home.domain.usecase.GetFeaturedAnimeUseCase
import com.yumedev.seijakulistkmp.features.home.presentation.HomeViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Módulo de Koin para Home feature
 * Flujo específico para Featured Carousel (aislado de otras secciones)
 */
val homeModule = module {
    // Data Layer - Featured Carousel Data Source
    singleOf(::FeaturedDataSourceImpl) bind FeaturedDataSource::class

    // Data Layer - Featured Repository
    singleOf(::FeaturedRepositoryImpl) bind FeaturedRepository::class

    // Domain Layer - Use Cases
    factoryOf(::GetFeaturedAnimeUseCase)

    // Presentation Layer - ViewModels
    viewModelOf(::HomeViewModel)
}
