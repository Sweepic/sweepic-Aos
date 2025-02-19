package com.umc.sweepic.di

import android.content.ContentResolver
import android.content.Context
import com.umc.sweepic.data.datasource.GalleryDataSource
import com.umc.sweepic.data.datasource.LoginDataSource
import com.umc.sweepic.data.datasource.HistoryDataSource
import com.umc.sweepic.data.datasource.sweep.SweepDataSource
import com.umc.sweepic.data.datasource.MemoDataSource
import com.umc.sweepic.data.datasource.MypageDataSource
import com.umc.sweepic.data.datasource.OnboardingDataSource
import com.umc.sweepic.data.datasourceImpl.sweep.GalleryDataSourceImpl
import com.umc.sweepic.data.datasourceImpl.sweep.LoginDataSourceImpl
import com.umc.sweepic.data.datasourceImpl.sweep.HistoryDataSourceImpl
import com.umc.sweepic.data.datasourceImpl.sweep.SweepDataSourceImpl
import com.umc.sweepic.data.datasourceImpl.sweep.MemoDataSourceImpl
import com.umc.sweepic.data.datasourceImpl.sweep.MypageDataSourceImpl
import com.umc.sweepic.data.datasourceImpl.sweep.OnboardingDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }
    @Provides
    @Singleton
    fun provideGalleryDataSource(galleryDataSourceImpl: GalleryDataSourceImpl): GalleryDataSource =
        galleryDataSourceImpl

    @Provides
    @Singleton
    fun provideSweepDataSource(sweepDataSourceImpl: SweepDataSourceImpl): SweepDataSource =
        sweepDataSourceImpl

    @Provides
    @Singleton
    fun provideMemoDataSource(memoDataSourceImpl: MemoDataSourceImpl) : MemoDataSource =
        memoDataSourceImpl

    @Provides
    @Singleton
    fun provideOnboardingDataSource(onboardingDataSourceImpl: OnboardingDataSourceImpl) : OnboardingDataSource =
        onboardingDataSourceImpl

    @Provides
    @Singleton
    fun provideLoginDataSource(loginDataSourceImpl: LoginDataSourceImpl) : LoginDataSource =
        loginDataSourceImpl

    @Provides
    @Singleton
    fun provideHistoryDataSource(historyDataSourceImpl: HistoryDataSourceImpl) : HistoryDataSource =
        historyDataSourceImpl

    @Provides
    @Singleton
    fun provideMypageDataSource(mypageDataSourceImpl: MypageDataSourceImpl) : MypageDataSource =
        mypageDataSourceImpl

}