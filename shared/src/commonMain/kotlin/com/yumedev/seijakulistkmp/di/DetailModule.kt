package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.features.detail.data.repository.FavoriteEpisodeRepositoryImpl
import com.yumedev.seijakulistkmp.features.detail.data.repository.MediaDetailRepositoryImpl
import com.yumedev.seijakulistkmp.features.detail.domain.repository.FavoriteEpisodeRepository
import com.yumedev.seijakulistkmp.features.detail.domain.repository.MediaDetailRepository
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.DeleteFavoriteEpisodeUseCase
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.GetAllFavoriteEpisodesUseCase
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.GetAnimeDetailUseCase
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.GetMangaDetailUseCase
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.ObserveAllFavoriteEpisodesUseCase
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.ObserveFavoriteEpisodesByMediaUseCase
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.ToggleFavoriteEpisodeUseCase
import com.yumedev.seijakulistkmp.features.detail.domain.usecase.UpdateFavoriteEpisodeUseCase
import com.yumedev.seijakulistkmp.features.detail.presentation.DetailViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val detailModule = module {
    singleOf(::MediaDetailRepositoryImpl) bind MediaDetailRepository::class
    singleOf(::FavoriteEpisodeRepositoryImpl) bind FavoriteEpisodeRepository::class

    factoryOf(::GetAnimeDetailUseCase)
    factoryOf(::GetMangaDetailUseCase)
    factoryOf(::ToggleFavoriteEpisodeUseCase)
    factoryOf(::ObserveFavoriteEpisodesByMediaUseCase)
    factoryOf(::GetAllFavoriteEpisodesUseCase)
    factoryOf(::ObserveAllFavoriteEpisodesUseCase)
    factoryOf(::UpdateFavoriteEpisodeUseCase)
    factoryOf(::DeleteFavoriteEpisodeUseCase)

    viewModelOf(::DetailViewModel)
}
