package com.obs.awss3.core

import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.options.StorageListOptions
import com.obs.awss3.listeners.S3FileListListener

class S3FileList(private val s3FileListListener: S3FileListListener) {

    fun getFiles(path: String) {
        Amplify.Storage.list(path,
            { result ->
                s3FileListListener.onSuccess(result)
            },
            { s3FileListListener.onFailure(it) }
        )
    }

    fun getFiles(path: String, options: StorageListOptions) {
        Amplify.Storage.list(path,options,
            { result ->
                s3FileListListener.onSuccess(result)
            },
            { s3FileListListener.onFailure(it) }
        )
    }

    suspend fun getFilesCoroutines(path: String) {
        try {
            com.amplifyframework.kotlin.core.Amplify.Storage.list(path).let {
                s3FileListListener.onSuccess(it)
            }
        } catch (error: StorageException) {
            s3FileListListener.onFailure(error)
        }
    }

    suspend fun getFilesCoroutines(path: String, options: StorageListOptions) {
        try {
            com.amplifyframework.kotlin.core.Amplify.Storage.list(path,options).let {
                s3FileListListener.onSuccess(it)
            }
        } catch (error: StorageException) {
            s3FileListListener.onFailure(error)
        }
    }

}