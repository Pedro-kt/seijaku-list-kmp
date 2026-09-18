package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.features.auth.domain.repository.AuthRepository
import com.yumedev.seijakulistkmp.features.auth.domain.usecase.*
import com.yumedev.seijakulistkmp.features.auth.presentation.AuthViewModel
import com.yumedev.seijakulistkmp.features.profile.data.remote.FirestoreProfileDataSource
import com.yumedev.seijakulistkmp.features.profile.domain.usecase.SyncProfileWithFirestoreUseCase
import com.yumedev.seijakulistkmp.features.tracking.data.remote.FirestoreMediaListDataSource
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.DeleteMediaListEntryFromFirestoreUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.SaveMediaListEntryToFirestoreUseCase
import com.yumedev.seijakulistkmp.features.tracking.domain.usecase.SyncMediaListWithFirestoreUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    singleOf(::FirestoreProfileDataSource)
    singleOf(::FirestoreMediaListDataSource)

    factoryOf(::LoginWithEmailUseCase)
    factoryOf(::RegisterWithEmailUseCase)
    factoryOf(::LoginWithGoogleUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::GetCurrentUserUseCase)
    factoryOf(::IsAuthenticatedUseCase)
    factoryOf(::SendPasswordResetUseCase)

    factoryOf(::SyncProfileWithFirestoreUseCase)
    factoryOf(::SyncMediaListWithFirestoreUseCase)
    factoryOf(::SaveMediaListEntryToFirestoreUseCase)
    factoryOf(::DeleteMediaListEntryFromFirestoreUseCase)

    viewModelOf(::AuthViewModel)
}
