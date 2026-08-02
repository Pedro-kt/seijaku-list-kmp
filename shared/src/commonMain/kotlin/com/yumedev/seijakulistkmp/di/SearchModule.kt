package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.features.search.presentation.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    viewModel { SearchViewModel(apolloClient = get(), mediaStringFormatter = get()) }
}
