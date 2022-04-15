package com.atilsamancioglu.solidstock.di

import android.app.Application
import androidx.room.Room
import com.atilsamancioglu.solidstock.data.local.StockDatabase
import com.atilsamancioglu.solidstock.data.remote.StockAPI
import com.atilsamancioglu.solidstock.data.remote.StockAPI.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStockApi() : StockAPI {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(StockAPI::class.java)
    }


   @Provides
   @Singleton
   fun provideStockDatabase(app : Application) : StockDatabase{
      return Room.databaseBuilder(
           app,
           StockDatabase::class.java,
           "stockdb.db"
      ).build()
   }

}