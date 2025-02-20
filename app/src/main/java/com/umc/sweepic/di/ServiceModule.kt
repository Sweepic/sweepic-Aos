package com.umc.sweepic.di

import com.umc.sweepic.data.service.SweepService
import com.umc.sweepic.data.service.MemoService
import com.umc.sweepic.data.service.OnboardingService
import com.umc.sweepic.data.service.TestService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    private inline fun <reified T> Retrofit.buildService(): T {
        return this.create(T::class.java)
    }

    @Provides
    @Singleton
    fun provideTestService(retrofit: Retrofit): TestService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideSweepService(retrofit: Retrofit): SweepService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideMemoService(retrofit: Retrofit) : MemoService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideOnboardingService(retrofit: Retrofit) : OnboardingService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit) : LoginService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideHistoryService(retrofit: Retrofit) : HistoryService {
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideMypageService(retrofit: Retrofit) : MypageService{
        return retrofit.buildService()
    }

    @Provides
    @Singleton
    fun provideChallengeService(retrofit: Retrofit): ChallengeService {
        return retrofit.buildService()
    }


    @Provides
    @Singleton
    fun provideAwardService(retrofit: Retrofit) : AwardService{
        return retrofit.buildService()
    }
}