package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.core.utils.ImageReader
import org.koin.dsl.module

actual val platformUtilsModule = module {
    single { ImageReader() }
}
