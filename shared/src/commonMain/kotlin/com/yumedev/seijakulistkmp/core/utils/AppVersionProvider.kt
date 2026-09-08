package com.yumedev.seijakulistkmp.core.utils

expect class AppVersionProvider() {
    fun getVersionName(): String
    fun getVersionCode(): String
}
