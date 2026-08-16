package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.features.character.data.repository.CharacterDetailRepositoryImpl
import com.yumedev.seijakulistkmp.features.character.domain.repository.CharacterDetailRepository
import com.yumedev.seijakulistkmp.features.character.domain.usecase.GetCharacterDetailUseCase
import com.yumedev.seijakulistkmp.features.character.presentation.CharacterViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val characterModule = module {
    singleOf(::CharacterDetailRepositoryImpl) bind CharacterDetailRepository::class

    factoryOf(::GetCharacterDetailUseCase)

    viewModelOf(::CharacterViewModel)
}
