package com.obs.awss3.listeners

import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.result.*


interface S3DownloadListener {
    fun onSuccess(onSuccess: StorageDownloadFileResult)
    fun onSuccess(onSuccess: StorageGetUrlResult)
    fun onFailure(onError: StorageException)
    fun onProgress(onProgress: StorageTransferProgress)
}