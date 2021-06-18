package com.obs.awss3.model

import java.io.File

/**
 * S3download error response
 *
 * @property id
 * @property message
 * @constructor Create empty S3download error response
 */
data class S3DownloadErrorResponse (
    val id: String,
    val message: String
        )

