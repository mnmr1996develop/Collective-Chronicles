package com.michaelrichards.collectivechronicles.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.michaelrichards.collectivechronicles.network.AuthAPI
import com.michaelrichards.collectivechronicles.repositories.auth.AuthRepository
import com.michaelrichards.collectivechronicles.repositories.auth.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideUserAuthAPI(): AuthAPI {

        val client = OkHttpClient.Builder()

        return Retrofit.Builder()
            .baseUrl("http://192.168.1.165:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(AuthAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthAPI, prefs: SharedPreferences): AuthRepository = AuthRepositoryImpl(api, prefs)

    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences = app
        .getSharedPreferences(
            "prefs",
            Context.MODE_PRIVATE
        )

}