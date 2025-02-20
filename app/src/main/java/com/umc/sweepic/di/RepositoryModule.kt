package com.umc.sweepic.di

import android.app.Application
import android.content.Context
import com.umc.sweepic.data.repositoryImpl.TestRepositoryImpl
import com.umc.sweepic.data.repositoryImpl.challenge.ChallengeRepositoryImpl
import com.umc.sweepic.data.repositoryImpl.sweep.AwardRepositoryImpl
import com.umc.sweepic.data.repositoryImpl.sweep.GalleryRepositoryImpl
import com.umc.sweepic.data.repositoryImpl.sweep.LoginRepositoryImpl
import com.umc.sweepic.data.repositoryImpl.sweep.HistoryRepositoryImpl
import com.umc.sweepic.data.repositoryImpl.sweep.SweepRepositoryImpl
import com.umc.sweepic.data.repositoryImpl.sweep.MemoRepositoryImpl
import com.umc.sweepic.data.repositoryImpl.sweep.MypageRepositoryImpl
import com.umc.sweepic.data.repositoryImpl.sweep.OnboardingRepositoryImpl
import com.umc.sweepic.data.repositoryImpl.sweep.TagboardRepositoryImpl
import com.umc.sweepic.data.service.TestService
import com.umc.sweepic.domain.repository.LoginRepository
import com.umc.sweepic.domain.repository.TestRepository
import com.umc.sweepic.domain.repository.challenge.ChallengeRepository
import com.umc.sweepic.domain.repository.sweep.AwardRepository
import com.umc.sweepic.domain.repository.sweep.GalleryRepository
import com.umc.sweepic.domain.repository.sweep.HistoryRepository
import com.umc.sweepic.domain.repository.sweep.SweepRepository
import com.umc.sweepic.domain.repository.sweep.MemoRepository
import com.umc.sweepic.domain.repository.sweep.MypageRepository
import com.umc.sweepic.domain.repository.sweep.OnboardingRepository
import com.umc.sweepic.domain.repository.sweep.TagboardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
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

    @Singleton
    @Provides
    fun providesMemoRepository(
        memoRepositoryImpl: MemoRepositoryImpl
    ): MemoRepository = memoRepositoryImpl

    @Singleton
    @Provides
    fun providesOnboardingRepository(
        onboardingRepositoryImpl: OnboardingRepositoryImpl
    ): OnboardingRepository = onboardingRepositoryImpl

    @Singleton
    @Provides
    fun providesLoginRepository(
        loginRepositoryImpl: LoginRepositoryImpl
    ): LoginRepository = loginRepositoryImpl

    @Singleton
    @Provides
    fun providesHistoryRepository(
        historyRepositoryImpl: HistoryRepositoryImpl
    ) : HistoryRepository = historyRepositoryImpl

    @Singleton
    @Provides
    fun providesMypageRepository(
        mypageRepositoryImpl: MypageRepositoryImpl
    ): MypageRepository = mypageRepositoryImpl

    @Singleton
    @Provides
    fun providesChallengeRepository(
        challengeRepositoryImpl: ChallengeRepositoryImpl
    ): ChallengeRepository = challengeRepositoryImpl

    @Provides
    @Singleton
    fun providesAwardRepository(
        awardRepositoryImpl: AwardRepositoryImpl
    ): AwardRepository = awardRepositoryImpl
    @Singleton
    @Provides
    fun provideTagboardRepository(
        tagboardRepositoryImpl: TagboardRepositoryImpl
    ): TagboardRepository = tagboardRepositoryImpl


}
