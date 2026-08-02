package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.core.util.MediaStringFormatter
import com.yumedev.seijakulistkmp.core.util.MediaStringFormatterImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule = module {
    singleOf(::MediaStringFormatterImpl) bind MediaStringFormatter::class
}
