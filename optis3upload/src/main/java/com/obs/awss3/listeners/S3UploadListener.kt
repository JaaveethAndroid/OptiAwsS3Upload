package com.obs.awss3.listeners

import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.result.StorageTransferProgress
import com.amplifyframework.storage.result.StorageUploadFileResult
import com.amplifyframework.storage.result.StorageUploadInputStreamResult


interface S3UploadListener {
    fun onSuccess(onSuccess: StorageUploadInputStreamResult)
    fun OnSuccess(onSuccess: StorageUploadFileResult)
    fun onFailure(onError: StorageException)
    fun onProgress(onProgress: StorageTransferProgress)
}