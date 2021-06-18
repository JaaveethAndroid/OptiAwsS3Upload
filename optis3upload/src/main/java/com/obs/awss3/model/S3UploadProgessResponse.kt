package com.obs.awss3.model

/**
 * S3upload progess response
 *
 * @property id
 * @property key
 * @property progress
 * @constructor Create empty S3upload progess response
 */
data class S3UploadProgessResponse (
       val id:String,
       val key: String,
       val progress: Double
        )

