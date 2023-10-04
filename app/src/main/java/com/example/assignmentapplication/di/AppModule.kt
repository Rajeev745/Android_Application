package com.example.assignmentapplication.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.assignmentapplication.api.RetrofitApi
import com.example.assignmentapplication.database.UserDataDatabase
import com.example.assignmentapplication.database.UserDataRepository
import com.example.assignmentapplication.database.UserDataTableDao
import com.example.assignmentapplication.utils.CONSTANTS
import com.example.assignmentapplication.viewmodel.DataViewmodel
import com.example.assignmentapplication.viewmodel.UserProfileViewmodel
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(CONSTANTS.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDataFromServer(retrofit: Retrofit): RetrofitApi {
        return retrofit.create(RetrofitApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): UserDataDatabase {
        return Room.databaseBuilder(context, UserDataDatabase::class.java, "user_data_table")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideStringItemDao(database: UserDataDatabase): UserDataTableDao {
        return database.userDataTableDao()
    }

    @Provides
    @Singleton
    fun provideStringItemRepository(
        dao: UserDataTableDao
    ): UserDataRepository {
        return UserDataRepository(dao)
    }

    @Provides
    @Singleton
    fun provideStringItemViewModel(
        retrofit: Retrofit,
        repository: UserDataRepository
    ): DataViewmodel {
        return DataViewmodel(retrofit, repository)
    }

    @Provides
    @Singleton
    fun getFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideUserProfileviewModel(
        sharedPreferences: SharedPreferences,
        @ApplicationContext context: Context
    ): UserProfileViewmodel {
        return UserProfileViewmodel(sharedPreferences, context)
    }

}