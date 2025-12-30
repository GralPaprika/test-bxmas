package space.carlosrdgz.test.vepormas.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import space.carlosrdgz.test.vepormas.BuildConfig
import space.carlosrdgz.test.vepormas.data.remote.api.LoremApiService
import space.carlosrdgz.test.vepormas.data.remote.api.PhotoApiService
import space.carlosrdgz.test.vepormas.data.repository.PhotoRepositoryImpl
import space.carlosrdgz.test.vepormas.domain.repository.PhotoRepository
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class JsonRetrofit

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class PlainTextRetrofit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    @Provides
    @Singleton
    @JsonRetrofit
    fun provideJsonRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.PHOTOS_API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @PlainTextRetrofit
    fun providePlainTextRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.LOREM_API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun providePhotoApiService(@JsonRetrofit retrofit: Retrofit): PhotoApiService =
        retrofit.create(PhotoApiService::class.java)

    @Provides
    @Singleton
    fun provideLoremApiService(@PlainTextRetrofit retrofit: Retrofit): LoremApiService =
        retrofit.create(LoremApiService::class.java)

    @Provides
    @Singleton
    fun providePhotoRepository(
        photoApiService: PhotoApiService,
        loremApiService: LoremApiService,
    ): PhotoRepository = PhotoRepositoryImpl(photoApiService, loremApiService)
}