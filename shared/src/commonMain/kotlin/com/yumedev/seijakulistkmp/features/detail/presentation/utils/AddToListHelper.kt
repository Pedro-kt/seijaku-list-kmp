package com.yumedev.seijakulistkmp.features.detail.presentation.utils

import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType
import com.yumedev.seijakulistkmp.features.detail.presentation.components.ListStatus

object AddToListHelper {

    fun getDefaultStatus(mediaType: MediaType, mediaStatus: String?): ListStatus {
        if (mediaStatus == "NOT_YET_RELEASED") {
            return if (mediaType == MediaType.ANIME) ListStatus.PLAN_TO_WATCH else ListStatus.PLAN_TO_READ
        }

        return if (mediaType == MediaType.ANIME) {
            ListStatus.WATCHING
        } else {
            ListStatus.READING
        }
    }

    fun isStatusEnabled(listStatus: ListStatus, mediaStatus: String?): Boolean {
        return when (mediaStatus) {
            "NOT_YET_RELEASED" -> {
                listStatus == ListStatus.PLAN_TO_WATCH || listStatus == ListStatus.PLAN_TO_READ
            }
            "RELEASING" -> {
                listStatus != ListStatus.COMPLETED
            }
            else -> {
                true
            }
        }
    }

    fun getStatusOptions(isAnime: Boolean): List<ListStatus> {
        return if (isAnime) {
            listOf(
                ListStatus.WATCHING,
                ListStatus.COMPLETED,
                ListStatus.PAUSED,
                ListStatus.DROPPED,
                ListStatus.PLAN_TO_WATCH
            )
        } else {
            listOf(
                ListStatus.READING,
                ListStatus.COMPLETED,
                ListStatus.PAUSED,
                ListStatus.DROPPED,
                ListStatus.PLAN_TO_READ
            )
        }
    }
}
