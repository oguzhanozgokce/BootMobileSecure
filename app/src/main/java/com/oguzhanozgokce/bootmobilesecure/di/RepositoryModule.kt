package com.oguzhanozgokce.bootmobilesecure.di

import com.oguzhanozgokce.bootmobilesecure.data.repository.MainRepositoryImpl
import com.oguzhanozgokce.bootmobilesecure.domain.repository.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMainRepository(repositoryImpl: MainRepositoryImpl): MainRepository
}