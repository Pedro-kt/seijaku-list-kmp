package com.yumedev.seijakulistkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform