package com.yumedev.seijakulistkmp.di

import com.yumedev.seijakulistkmp.features.schedule.data.datasource.AiringScheduleDataSource
import com.yumedev.seijakulistkmp.features.schedule.data.datasource.AiringScheduleDataSourceImpl
import com.yumedev.seijakulistkmp.features.schedule.data.repository.AiringScheduleRepositoryImpl
import com.yumedev.seijakulistkmp.features.schedule.domain.repository.AiringScheduleRepository
import com.yumedev.seijakulistkmp.features.schedule.domain.usecase.GetDayScheduleUseCase
import com.yumedev.seijakulistkmp.features.schedule.domain.usecase.GetWeeklyScheduleUseCase
import com.yumedev.seijakulistkmp.features.schedule.presentation.AiringScheduleViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val scheduleModule = module {
    singleOf(::AiringScheduleDataSourceImpl) bind AiringScheduleDataSource::class

    singleOf(::AiringScheduleRepositoryImpl) bind AiringScheduleRepository::class

    factoryOf(::GetWeeklyScheduleUseCase)
    factoryOf(::GetDayScheduleUseCase)

    viewModelOf(::AiringScheduleViewModel)
}
