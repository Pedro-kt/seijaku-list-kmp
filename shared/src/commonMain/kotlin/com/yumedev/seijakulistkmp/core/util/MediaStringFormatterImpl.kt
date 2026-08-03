package com.yumedev.seijakulistkmp.core.util

import org.jetbrains.compose.resources.getString
import seijakulistkmp.shared.generated.resources.*

class MediaStringFormatterImpl : MediaStringFormatter {
    override suspend fun formatMediaFormat(format: String?): String? {
        return when (format) {
            "TV" -> getString(Res.string.format_tv)
            "TV_SHORT" -> getString(Res.string.format_tv_short)
            "MOVIE" -> getString(Res.string.format_movie)
            "SPECIAL" -> getString(Res.string.format_special)
            "OVA" -> getString(Res.string.format_ova)
            "ONA" -> getString(Res.string.format_ona)
            "MUSIC" -> getString(Res.string.format_music)
            else -> null
        }
    }

    override suspend fun formatEpisodes(episodes: Int?): String? {
        return episodes?.let {
            "$it ${getString(Res.string.episodes_suffix)}"
        }
    }

    override suspend fun formatChapters(chapters: Int?): String? {
        return chapters?.let {
            "$it Ch"
        }
    }

    override suspend fun formatVolumes(volumes: Int?): String? {
        return volumes?.let {
            "$it Vol"
        }
    }
}
