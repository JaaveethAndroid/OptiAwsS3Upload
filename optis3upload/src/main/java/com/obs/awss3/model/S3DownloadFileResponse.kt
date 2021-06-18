package com.obs.awss3.model

import java.io.File

/**
 * S3download file response
 *
 * @property id
 * @property file
 * @property key
 * @constructor Create empty S3download file response
 */
data class S3DownloadFileResponse (
    val id: String,
    val file: File,
    val key: String
        )

