package com.yumedev.seijakulistkmp.features.detail.presentation

import com.yumedev.seijakulistkmp.features.detail.domain.model.Chapter
import com.yumedev.seijakulistkmp.features.detail.domain.model.Character
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaDetail
import com.yumedev.seijakulistkmp.features.detail.domain.model.MediaType

object DetailScreenPreview {

    val sampleMangaDetail = MediaDetail(
        id = 1,
        title = "Berserk",
        titleNative = "ベルセカ",
        coverImageUrl = null,
        bannerImageUrl = null,
        type = MediaType.MANGA,
        format = "Manga",
        demographic = "Seinen",
        year = 1989,
        status = "Publicándose",
        episodes = null,
        chapters = 383,
        volumes = 42,
        startDate = "ago 1989",
        endDate = "presente",
        serialization = "Young Animal",
        averageScore = 94.7,
        rankingPosition = 1,
        popularityPosition = 12,
        totalVotes = 286400,
        description = "Guts, un espadachín marcado desde su nacimiento, sobrevive como mercenario hasta que su encuentro con Griffith y la Banda del Halcón le da algo parecido a un hogar. La ambición de Griffith lo lleva a un sacrificio que cambiará todo para siempre.",
        author = "Miura, Kentarou",
        artist = "Studio Gaga",
        studio = null,
        license = "Dark Horse",
        genres = listOf("Acción", "Aventura", "Drama", "Fantasía oscura", "Horror"),
        mainCharacters = listOf(
            Character(1, "Guts", null, "Principal"),
            Character(2, "Griffith", null, "Principal"),
            Character(3, "Casca", null, "Principal"),
            Character(4, "Puck", null, "Secundario"),
            Character(5, "Schierke", null, "Secundario")
        ),
        episodes_list = null,
        chapters_list = listOf(
            Chapter(1, "El Guerrero Negro", 1, "1990", 4.8),
            Chapter(2, "La Chica del Templo del Halcón", 1, "1990", 4.7),
            Chapter(3, "El Guardián del Deseo", 1, "1990", 4.7)
        ),
        images = emptyList(),
        isFavorite = false,
        isInList = false
    )

    val sampleAnimeDetail = MediaDetail(
        id = 2,
        title = "Attack on Titan",
        titleNative = "進撃の巨人",
        coverImageUrl = null,
        bannerImageUrl = null,
        type = MediaType.ANIME,
        format = "TV",
        demographic = "Shounen",
        year = 2013,
        status = "Finalizado",
        episodes = 87,
        chapters = null,
        volumes = null,
        startDate = "abr 2013",
        endDate = "nov 2023",
        serialization = null,
        averageScore = 91.2,
        rankingPosition = 3,
        popularityPosition = 1,
        totalVotes = 1250000,
        description = "Hace varios siglos, la humanidad fue diezmada por los Titanes, criaturas gigantes que devoran humanos. Los sobrevivientes se refugiaron detrás de enormes muros. La historia sigue a Eren Yeager quien, tras la destrucción de su ciudad natal, jura vengarse de todos los Titanes.",
        author = null,
        artist = null,
        studio = "MAPPA, Wit Studio",
        license = "Crunchyroll",
        genres = listOf("Acción", "Drama", "Fantasía", "Militar", "Misterio"),
        mainCharacters = listOf(
            Character(1, "Eren Yeager", null, "Principal"),
            Character(2, "Mikasa Ackerman", null, "Principal"),
            Character(3, "Armin Arlert", null, "Principal"),
            Character(4, "Levi Ackerman", null, "Principal")
        ),
        episodes_list = emptyList(),
        chapters_list = null,
        images = emptyList(),
        isFavorite = true,
        isInList = true
    )
}
