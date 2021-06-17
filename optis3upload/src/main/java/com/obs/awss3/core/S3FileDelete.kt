package com.obs.awss3.core

import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.options.StorageListOptions
import com.amplifyframework.storage.options.StorageRemoveOptions
import com.obs.awss3.listeners.S3RemoveListenerCallback
import com.obs.awss3.listeners.S3RemoveSession
import com.obs.awss3.model.S3RemoveFileErrorResponse
import com.obs.awss3.model.S3RemoveFileResponse

class S3FileDelete(private val s3RemoveListener: S3RemoveListenerCallback): S3RemoveSession {

    override fun deleteFile(id: String, key: String) {
        Amplify.Storage.remove(key,
            { s3RemoveListener.onRemoveSuccess(S3RemoveFileResponse(id, it.key)) },
            { it.message?.let {
                s3RemoveListener.onRemoveError(S3RemoveFileErrorResponse(id,it))
            } }
        )
    }

    override fun deleteFile(id: String,key: String,options: StorageRemoveOptions) {
        Amplify.Storage.remove(key,options,
            { s3RemoveListener.onRemoveSuccess(S3RemoveFileResponse(id, it.key)) },
            { it.message?.let {
                s3RemoveListener.onRemoveError(S3RemoveFileErrorResponse(id,it))
            } }
        )
    }

    override suspend fun deleteFileCoroutines(id: String,key: String) {
        try {
            val result = com.amplifyframework.kotlin.core.Amplify.Storage.remove(key)
            s3RemoveListener.onRemoveSuccess(S3RemoveFileResponse(id, result.key))
        } catch (error: StorageException) {
            error.message?.let {
                s3RemoveListener.onRemoveError(S3RemoveFileErrorResponse(id,it))
            }
        }
    }

}