package com.example.final_project_assignment_hansenbillyramades.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.final_project_assignment_hansenbillyramades.data.source.local.AddressLocalDataSource
import com.example.final_project_assignment_hansenbillyramades.data.source.local.AddressLocalDataSourceImpl
import com.example.final_project_assignment_hansenbillyramades.data.source.local.PreferenceDataStore
import com.example.final_project_assignment_hansenbillyramades.data.source.local.StomazonDatabase
import com.example.final_project_assignment_hansenbillyramades.data.source.local.dataStore
import com.example.final_project_assignment_hansenbillyramades.data.source.network.ApiService
import com.example.final_project_assignment_hansenbillyramades.data.source.network.UserRemoteDataSource
import com.example.final_project_assignment_hansenbillyramades.data.source.network.UserRemoteDataSourceImpl
import com.example.final_project_assignment_hansenbillyramades.domain.repository.AddressRepository
import com.example.final_project_assignment_hansenbillyramades.domain.repository.AddressRepositoryImpl
import com.example.final_project_assignment_hansenbillyramades.domain.repository.OrderRepository
import com.example.final_project_assignment_hansenbillyramades.domain.repository.OrderRepositoryImpl
import com.example.final_project_assignment_hansenbillyramades.domain.repository.ProductRepository
import com.example.final_project_assignment_hansenbillyramades.domain.repository.ProductRepositoryImpl
import com.example.final_project_assignment_hansenbillyramades.domain.repository.TransactionOrderRepository
import com.example.final_project_assignment_hansenbillyramades.domain.repository.TransactionOrderRepositoryImpl
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.AddressUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val SECRET_APP_KEY = "B%So@A\$J7XHR!0M;<(@hzlYfV]T.[T"


    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(createHeaderInterceptor())
            .addInterceptor(provideLoggingInterceptor())
            .build()
        return Retrofit.Builder()
            .baseUrl("https://phincon-academy-api.onrender.com/phincon/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(apiService: ApiService): UserRemoteDataSource {
        return UserRemoteDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideProductRepository(remoteDataSource: UserRemoteDataSource): ProductRepository {
        return ProductRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(remoteDataSource: UserRemoteDataSource): OrderRepository {
        return OrderRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideTransactionOrderRepository(remoteDataSource: UserRemoteDataSource): TransactionOrderRepository {
        return TransactionOrderRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideAddressUseCase(addressRepository: AddressRepository): AddressUseCase {
        return AddressUseCase(addressRepository)
    }

    @Provides
    @Singleton
    fun provideAddressRepository(addressLocalDataSource: AddressLocalDataSource): AddressRepository {
        return AddressRepositoryImpl(addressLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideAddressLocalDataSource(stomazonDatabase: StomazonDatabase): AddressLocalDataSource {
        return AddressLocalDataSourceImpl(stomazonDatabase.addressDao())
    }


    @Provides
    @Singleton
    fun providePreferenceDataStore(@ApplicationContext context: Context): PreferenceDataStore {
        return PreferenceDataStore.getInstance(context.dataStore)
    }


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StomazonDatabase {
        return Room.databaseBuilder(
            context,
            StomazonDatabase::class.java,
            "stomazon_database"
        ).build()
    }


    private fun createHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original: Request = chain.request()
            val requestWithHeaders = original.newBuilder()
                .header("x-secret-app", SECRET_APP_KEY)
                .header("x-user-id", "1")
                .build()

            chain.proceed(requestWithHeaders)
        }
    }
}
