package com.vegcale.architecture.data.di

import com.vegcale.architecture.data.OfflineUserDataRepository
import com.vegcale.architecture.data.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsUserDataRepository(
        userDataRepository: OfflineUserDataRepository,
    ): UserDataRepository
}