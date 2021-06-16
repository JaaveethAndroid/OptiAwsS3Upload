package com.obs.awss3.model

import java.io.File

data class S3DownloadFileResponse (
    val id: String,
    val file: File,
    val key: String
        )

