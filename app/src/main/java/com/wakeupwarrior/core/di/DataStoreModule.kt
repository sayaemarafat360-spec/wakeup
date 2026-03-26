package com.wakeupwarrior.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.wakeupwarrior.core.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.Preferences.DATASTORE_NAME)

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.dataStore
    }
}
