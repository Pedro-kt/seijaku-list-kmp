package com.yumedev.seijakulistkmp.features.home.domain.usecase

import com.yumedev.seijakulistkmp.data.remote.graphql.GetMangaHomeDataQuery
import com.yumedev.seijakulistkmp.features.home.data.datasource.MangaHomeDataSource

class GetMangaHomeDataUseCase(
    private val mangaHomeDataSource: MangaHomeDataSource
) {
    suspend operator fun invoke(
        isAdult: Boolean = false
    ): Result<GetMangaHomeDataQuery.Data> {
        return try {
            val data = mangaHomeDataSource.getMangaHomeData(
                featuredPerPage = 5,
                publishingPerPage = 10,
                popularPerPage = 10,
                topRatedPerPage = 10,
                recentlyAddedPerPage = 10,
                manhwaPerPage = 10,
                manhwaCountry = "KR",
                isAdult = isAdult
            )
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
