package com.yumedev.seijakulistkmp.di

import org.koin.core.module.Module

expect val platformNetworkModule: Module

val networkModule = platformNetworkModule
