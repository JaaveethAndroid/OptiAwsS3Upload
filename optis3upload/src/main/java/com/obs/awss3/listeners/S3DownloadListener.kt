package com.obs.awss3.listeners

import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.result.*
import com.obs.awss3.model.S3DownloadErrorResponse
import com.obs.awss3.model.S3DownloadFileResponse
import com.obs.awss3.model.S3DownloadProgessResponse
import com.obs.awss3.model.S3DownloadURLResponse


interface S3DownloadListener {
    fun onDownloadSuccess(onSuccess: S3DownloadFileResponse)
    fun onDownloadSuccess(onSuccess: S3DownloadURLResponse)
    fun onDownloadFailure(onError: S3DownloadErrorResponse)
    fun onDownloadProgress(onProgress: S3DownloadProgessResponse)
}