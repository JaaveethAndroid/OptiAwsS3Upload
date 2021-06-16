package com.obs.awss3.listeners

import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.result.StorageListResult


interface S3FileListListener {
    fun onSuccess(onSuccess: StorageListResult)
    fun onFailure(onError: StorageException)
}