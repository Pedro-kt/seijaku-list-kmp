package com.yumedev.seijakulistkmp.features.detail.presentation.utils

import androidx.compose.ui.graphics.Color

object DetailFormatters {

    fun formatScore(score: Double): String {
        val formatted = (score / 10.0 * 100).toInt() / 100.0
        return formatted.toString().take(4).trimEnd('.')
    }

    fun formatVotes(votes: Int?, votesLabel: String): String {
        votes ?: return ""
        return when {
            votes >= 1_000_000 -> {
                val m = (votes / 100_000.0).toInt() / 10.0
                "${m}M $votesLabel"
            }
            votes >= 1_000 -> {
                val k = (votes / 100.0).toInt() / 10.0
                "${k}K $votesLabel"
            }
            else -> "$votes $votesLabel"
        }
    }

    fun buildMetadataString(
        year: Int?,
        format: String?,
        chapters: Int?,
        volumes: Int?,
        episodes: Int?,
        statusText: String?
    ): String {
        return buildList {
            year?.let { add(it.toString()) }
            format?.let { add(it) }
            chapters?.let { add("$it cap") }
            volumes?.let { add("$it vol") }
            episodes?.let { add("$it ep") }
            statusText?.let { add(it) }
        }.joinToString(" · ")
    }

    fun formatNextAiringText(
        episode: Int,
        timeUntilAiring: Long,
        timeMinutesLabel: String,
        timeHoursLabel: String,
        timeDaysLabel: String
    ): String {
        val (timeValue, timeUnit) = when {
            timeUntilAiring < 3600 -> (timeUntilAiring / 60).toInt() to timeMinutesLabel
            timeUntilAiring < 86400 -> (timeUntilAiring / 3600).toInt() to timeHoursLabel
            else -> (timeUntilAiring / 86400).toInt() to timeDaysLabel
        }
        return "Ep. $episode en $timeValue$timeUnit"
    }

    fun parseHexColor(colorString: String, defaultColor: Color): Color {
        return try {
            val hex = colorString.removePrefix("#")
            val colorInt = hex.toLongOrNull(16) ?: return defaultColor
            Color(colorInt or 0xFF000000)
        } catch (e: Exception) {
            defaultColor
        }
    }

    fun getContrastingTextColor(backgroundColor: Color): Color {
        val red = backgroundColor.red
        val green = backgroundColor.green
        val blue = backgroundColor.blue

        val luminance = (0.299 * red + 0.587 * green + 0.114 * blue)

        return if (luminance > 0.5) {
            Color.Black
        } else {
            Color.White
        }
    }

    fun formatDate(year: Int?, month: Int?, day: Int?): String? {
        return buildString {
            day?.let {
                append(it.toString().padStart(2, '0'))
                append(" ")
            }
            month?.let {
                val monthName = when (it) {
                    1 -> "ene"
                    2 -> "feb"
                    3 -> "mar"
                    4 -> "abr"
                    5 -> "may"
                    6 -> "jun"
                    7 -> "jul"
                    8 -> "ago"
                    9 -> "sep"
                    10 -> "oct"
                    11 -> "nov"
                    12 -> "dic"
                    else -> return@let
                }
                append(monthName)
                append(" ")
            }
            year?.let { append(it) }
        }.takeIf { it.isNotBlank() }
    }
}
