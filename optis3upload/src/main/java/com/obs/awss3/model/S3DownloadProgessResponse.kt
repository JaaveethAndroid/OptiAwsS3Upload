package com.obs.awss3.model

data class S3DownloadProgessResponse (
    val id: String,
       val key: String,
       val progress: Double
        )

