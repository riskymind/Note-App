package com.asterisk.noteapp.di

import android.content.Context
import androidx.room.Room
import com.asterisk.noteapp.data.local.NoteDatabase
import com.asterisk.noteapp.data.local.dao.NoteDao
import com.asterisk.noteapp.data.remote.NoteApi
import com.asterisk.noteapp.util.Constants.BASE_URL
import com.asterisk.noteapp.util.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideSessionManager(
        @ApplicationContext context: Context,
    ) =
        SessionManager(context)

    @Singleton
    @Provides
    fun provideNoteDatabase(
        @ApplicationContext context: Context,
    ): NoteDatabase = Room.databaseBuilder(
        context, NoteDatabase::class.java, "note_db"
    ).fallbackToDestructiveMigration()
        .build()


    @Singleton
    @Provides
    fun provideNoteDao(
        noteDatabase: NoteDatabase,
    ): NoteDao = noteDatabase.getNoteDao()


    @Singleton
    @Provides
    fun provideNoteApi(): NoteApi {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NoteApi::class.java)
    }

}