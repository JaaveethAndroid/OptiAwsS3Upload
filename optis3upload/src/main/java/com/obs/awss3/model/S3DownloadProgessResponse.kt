package com.obs.awss3.model

/**
 * S3download progess response
 *
 * @property id
 * @property key
 * @property progress
 * @constructor Create empty S3download progess response
 */
data class S3DownloadProgessResponse (
    val id: String,
       val key: String,
       val progress: Double
        )

