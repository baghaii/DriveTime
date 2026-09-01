package com.sepidehmiller.drivetime.di

import android.content.Context
import androidx.room3.Room
import com.sepidehmiller.drivetime.data.source.LocalRepository
import com.sepidehmiller.drivetime.data.source.LocalRepositoryImpl
import com.sepidehmiller.drivetime.data.source.local.DriveTimeDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindLocalRepository(repository: LocalRepositoryImpl): LocalRepository
}
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): DriveTimeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            DriveTimeDatabase::class.java,
            "DriveTime.db"
        ).build()
    }
    @Provides
    fun provideDriveTimeDao(database: DriveTimeDatabase) = database.driveTimeDao()
}
