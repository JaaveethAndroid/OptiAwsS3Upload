package com.obs.awss3.model

import java.util.*


/**
 * Storage item response
 *
 * @property key
 * @property size
 * @property lastModified
 * @property eTag
 * @constructor Create empty Storage item response
 */
data class StorageItemResponse (
    val key: String,
    val size: Long,
    val lastModified: Date,
    val eTag: String
)