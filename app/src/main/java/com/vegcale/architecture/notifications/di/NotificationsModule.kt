package com.vegcale.architecture.notifications.di

import com.vegcale.architecture.notifications.Notifier
import com.vegcale.architecture.notifications.SystemTrayNotifier
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationsModule {
    @Binds
    abstract fun bindNotifier(
        systemTrayNotifier: SystemTrayNotifier,
    ): Notifier
}