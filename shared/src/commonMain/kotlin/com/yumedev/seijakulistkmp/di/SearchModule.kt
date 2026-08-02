package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.features.search.presentation.SearchViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val searchModule = module {
    viewModelOf(::SearchViewModel)
}
