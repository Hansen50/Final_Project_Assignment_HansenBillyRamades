package com.example.final_project_assignment_hansenbillyramades.di

import android.content.Context
import androidx.room.Room
import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartLocalDataSource
import com.example.final_project_assignment_hansenbillyramades.data.source.local.CartLocalDataSourceImpl
import com.example.final_project_assignment_hansenbillyramades.data.source.local.preferences.PreferenceDataStore
import com.example.final_project_assignment_hansenbillyramades.data.source.local.room.StomazonDatabase
import com.example.final_project_assignment_hansenbillyramades.data.source.local.preferences.dataStore
import com.example.final_project_assignment_hansenbillyramades.data.source.network.ApiService
import com.example.final_project_assignment_hansenbillyramades.data.source.network.FirebaseAuthDataSource
import com.example.final_project_assignment_hansenbillyramades.data.source.network.FirebaseAuthDataSourceImpl
import com.example.final_project_assignment_hansenbillyramades.data.source.network.UserRemoteDataSource
import com.example.final_project_assignment_hansenbillyramades.data.source.network.UserRemoteDataSourceImpl
import com.example.final_project_assignment_hansenbillyramades.domain.repository.CartRepository
import com.example.final_project_assignment_hansenbillyramades.domain.repository.CartRepositoryImpl
import com.example.final_project_assignment_hansenbillyramades.domain.repository.FirebaseAuthRepository
import com.example.final_project_assignment_hansenbillyramades.domain.repository.FirebaseAuthRepositoryImpl
import com.example.final_project_assignment_hansenbillyramades.domain.repository.OrderRepository
import com.example.final_project_assignment_hansenbillyramades.domain.repository.OrderRepositoryImpl
import com.example.final_project_assignment_hansenbillyramades.domain.repository.ProductRepository
import com.example.final_project_assignment_hansenbillyramades.domain.repository.ProductRepositoryImpl
import com.example.final_project_assignment_hansenbillyramades.domain.repository.TransactionOrderRepository
import com.example.final_project_assignment_hansenbillyramades.domain.repository.TransactionOrderRepositoryImpl
import com.example.final_project_assignment_hansenbillyramades.domain.usecase.CartUseCase
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
    fun provideCartUseCase(cartRepository: CartRepository): CartUseCase {
        return CartUseCase(cartRepository)
    }

    @Provides
    @Singleton
    fun provideCartRepository(cartLocalDataSource: CartLocalDataSource): CartRepository {
        return CartRepositoryImpl(cartLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideCartLocalDataSource(stomazonDatabase: StomazonDatabase): CartLocalDataSource {
        return CartLocalDataSourceImpl(stomazonDatabase.cartDao())
    }


    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthDataSource(firebaseAuth: FirebaseAuth): FirebaseAuthDataSource {
        return FirebaseAuthDataSourceImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthRepository(firebaseAuthDataSource: FirebaseAuthDataSource): FirebaseAuthRepository {
        return FirebaseAuthRepositoryImpl(firebaseAuthDataSource)
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
