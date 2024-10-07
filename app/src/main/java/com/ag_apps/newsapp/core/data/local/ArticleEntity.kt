package com.ag_apps.newsapp.core.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ArticleEntity(

    @PrimaryKey(autoGenerate = false)
    val articleId: String,

    val title: String,
    val description: String,
    val content: String,
    val pubDate: String,
    val sourceName: String,
    val imageUrl: String
)