package com.yumedev.seijakulistkmp.di

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.yumedev.seijakulistkmp.features.settings.data.repository.SettingsRepositoryImpl
import com.yumedev.seijakulistkmp.features.settings.domain.repository.SettingsRepository
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.GetLanguageModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.GetSfwModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.GetThemeModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.SetLanguageModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.SetSfwModeUseCase
import com.yumedev.seijakulistkmp.features.settings.domain.usecase.SetThemeModeUseCase
import com.yumedev.seijakulistkmp.features.settings.presentation.SettingsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val settingsModule = module {
    single<ObservableSettings> { get<Settings>() as ObservableSettings }

    singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class

    factoryOf(::GetThemeModeUseCase)
    factoryOf(::SetThemeModeUseCase)
    factoryOf(::GetLanguageModeUseCase)
    factoryOf(::SetLanguageModeUseCase)
    factoryOf(::GetSfwModeUseCase)
    factoryOf(::SetSfwModeUseCase)

    viewModelOf(::SettingsViewModel)
}
