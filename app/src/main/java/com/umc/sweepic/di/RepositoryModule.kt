package com.umc.sweepic.di

import android.app.Application
import android.content.Context
import com.umc.sweepic.data.repositoryImpl.TestRepositoryImpl
import com.umc.sweepic.data.repositoryImpl.sweep.GalleryRepositoryImpl
import com.umc.sweepic.data.repositoryImpl.sweep.SweepRepositoryImpl
import com.umc.sweepic.data.service.TestService
import com.umc.sweepic.domain.repository.TestRepository
import com.umc.sweepic.domain.repository.sweep.GalleryRepository
import com.umc.sweepic.domain.repository.sweep.SweepRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context = application

    // 스코프 애노테이션이 있음
    // 해당하는 Hilt 컴포넌트의 수명동안 매 요청에 동일 인스턴스를 반환
    // 다음의 경우 viewModel의 수명동안 동일 인스턴스를 반환
    @Singleton
    @Provides
    fun providesTestRepository(
        testService: TestService
    ): TestRepository = TestRepositoryImpl(testService)

    @Singleton
    @Provides
    fun providesGalleryRepository(
        galleryRepositoryImpl: GalleryRepositoryImpl
    ): GalleryRepository = galleryRepositoryImpl

    @Singleton
    @Provides
    fun providesSweepRepository(
        sweepRepositoryImpl: SweepRepositoryImpl
    ): SweepRepository = sweepRepositoryImpl

}
