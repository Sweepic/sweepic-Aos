package com.umc.sweepic.di

import android.content.ContentResolver
import android.content.Context
import com.umc.sweepic.data.datasource.GalleryDataSource
import com.umc.sweepic.data.datasource.MemoDataSource
import com.umc.sweepic.data.datasourceImpl.sweep.GalleryDataSourceImpl
import com.umc.sweepic.data.datasourceImpl.sweep.MemoDataSourceImpl
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
    fun provideMemoDataSource(memoDataSourceImpl: MemoDataSourceImpl) : MemoDataSource = memoDataSourceImpl
}