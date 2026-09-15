package com.yumedev.seijakulistkmp.di

import org.koin.core.module.Module

expect fun platformAuthModule(googleWebClientId: String): Module
