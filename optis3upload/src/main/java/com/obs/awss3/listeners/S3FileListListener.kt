package com.obs.awss3.listeners

import com.amplifyframework.storage.options.StorageListOptions
import com.obs.awss3.model.StorageItemResponse

interface S3FileListSession {
    fun getFiles(path: String)
    fun getFiles(path: String, options: StorageListOptions)
    suspend fun getFilesCoroutines(path: String)
    suspend fun getFilesCoroutines(path: String, options: StorageListOptions)
}

interface S3FileListListenerCallback {
    fun onListSuccess(onSuccess: List<StorageItemResponse>)
    fun onListFailure(onError: String)
}