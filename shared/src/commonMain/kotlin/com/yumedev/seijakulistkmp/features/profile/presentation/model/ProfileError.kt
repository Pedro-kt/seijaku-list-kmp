package com.yumedev.seijakulistkmp.features.profile.presentation.model

sealed class ProfileError {
    data object LoadingError : ProfileError()
    data object SavingError : ProfileError()
    data object UpdatingStatsError : ProfileError()
    data object AnilistSyncUnavailable : ProfileError()
}
