package com.obs.awss3.model

import java.util.*


data class StorageItemResponse (
    val key: String,
    val size: Long,
    val lastModified: Date,
    val eTag: String
)