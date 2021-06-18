package com.obs.awss3.listeners

import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.options.StorageRemoveOptions
import com.amplifyframework.storage.result.StorageListResult
import com.amplifyframework.storage.result.StorageRemoveResult
import com.obs.awss3.model.S3RemoveFileErrorResponse
import com.obs.awss3.model.S3RemoveFileResponse

/**
 * S3remove session
 *
 * @constructor Create empty S3remove session
 */
interface S3RemoveSession {
    /**
     * Delete file
     *
     * @param id
     * @param key
     */
    fun deleteFile(id: String, key: String)

    /**
     * Delete file
     *
     * @param id
     * @param key
     * @param options
     */
    fun deleteFile(id: String, key: String, options: StorageRemoveOptions)

    /**
     * Delete file coroutines
     *
     * @param id
     * @param key
     */
    suspend fun deleteFileCoroutines(id: String, key: String)
}

/**
 * S3remove listener callback
 *
 * @constructor Create empty S3remove listener callback
 */
interface S3RemoveListenerCallback {
    /**
     * On remove success
     *
     * @param onSuccess
     */
    fun onRemoveSuccess(onSuccess: S3RemoveFileResponse)

    /**
     * On remove error
     *
     * @param onError
     */
    fun onRemoveError(onError: S3RemoveFileErrorResponse)
}