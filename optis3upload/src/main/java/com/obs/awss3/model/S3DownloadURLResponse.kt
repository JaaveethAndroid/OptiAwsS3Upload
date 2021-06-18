package com.obs.awss3.model

import java.io.File
import java.net.URL

/**
 * S3download u r l response
 *
 * @property id
 * @property url
 * @constructor Create empty S3download u r l response
 */
data class S3DownloadURLResponse (
    val id: String,
    val url: URL
        )

