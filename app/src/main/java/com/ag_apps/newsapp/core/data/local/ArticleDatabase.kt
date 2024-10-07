package com.ag_apps.newsapp.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * @author Ahmed Guedmioui
 */

@Database(
    entities = [ArticleEntity::class],
    version = 1
)
abstract class ArticleDatabase: RoomDatabase() {
    abstract val dao: ArticlesDao
}