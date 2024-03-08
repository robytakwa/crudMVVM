package com.danamon.robby.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.danamon.robby.model.album.AlbumItem
import com.danamon.robby.model.photos.PhotosItem

/**
 * Database management class
 */
@Database(
    entities = [AlbumItem::class, PhotosItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao?
    abstract fun photoDao(): PhotosDao?
}