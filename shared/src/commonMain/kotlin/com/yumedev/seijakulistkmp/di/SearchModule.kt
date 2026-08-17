package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.features.search.data.RecentSearchRepository
import com.yumedev.seijakulistkmp.features.search.presentation.SearchViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    singleOf(::RecentSearchRepository)

    viewModel {
        SearchViewModel(
            apolloClient = get(),
            mediaStringFormatter = get(),
            recentSearchRepository = get(),
            settingsRepository = get()
        )
    }
}
