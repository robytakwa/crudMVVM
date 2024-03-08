package com.danamon.robby.di

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.danamon.robby.db.AlbumDao
import com.danamon.robby.db.AppDatabase
import com.danamon.robby.db.PhotosDao
import com.danamon.robby.model.album.AlbumItem
import com.danamon.robby.model.photos.PhotosItem
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Provides
import javax.inject.Singleton

/**
 * Provider of singleton objects of Database classes
 *
 */
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    companion object {
        /**
         * Provider of Room Database
         * @return the AppDatabase class
         */
        @Provides
        @Singleton
        fun provideAlbumDB(application: Application?): AppDatabase {
            return Room.databaseBuilder(application!!, AppDatabase::class.java, "Albums")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }

        /**
         * Provider of AlbumList
         * @return the AlbumList
         */
        @Provides
        @Singleton
        fun provideAlbumDao(albumDao: AlbumDao): LiveData<List<AlbumItem?>?> {
            return albumDao.getAlbum()
        }

        /**
         * Provider of PhotosList
         * @return the PhotosList
         */
        @Provides
        @Singleton
        fun providePhotosDao(photoDao: PhotosDao): LiveData<List<PhotosItem?>?> {
            return photoDao.getPhotos()
        }
    }
}