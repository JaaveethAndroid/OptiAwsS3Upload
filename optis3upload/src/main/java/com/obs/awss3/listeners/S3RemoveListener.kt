package com.obs.awss3.listeners

import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.result.StorageListResult
import com.amplifyframework.storage.result.StorageRemoveResult
import com.obs.awss3.model.S3RemoveFileErrorResponse
import com.obs.awss3.model.S3RemoveFileResponse


interface S3RemoveListener {
    fun onRemoveSuccess(onSuccess: S3RemoveFileResponse)
    fun onRemoveError(onError: S3RemoveFileErrorResponse)
}