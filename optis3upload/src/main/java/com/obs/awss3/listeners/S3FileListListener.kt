package com.obs.awss3.listeners

import com.amplifyframework.storage.options.StorageListOptions
import com.obs.awss3.model.StorageItemResponse

/**
 * S3file list session
 *
 * @constructor Create empty S3file list session
 */
interface S3FileListSession {
    /**
     * Get files
     *
     * @param path
     */
    fun getFiles(path: String)

    /**
     * Get files
     *
     * @param path
     * @param options
     */
    fun getFiles(path: String, options: StorageListOptions)

    /**
     * Get files coroutines
     *
     * @param path
     */
    suspend fun getFilesCoroutines(path: String)

    /**
     * Get files coroutines
     *
     * @param path
     * @param options
     */
    suspend fun getFilesCoroutines(path: String, options: StorageListOptions)
}

/**
 * S3file list listener callback
 *
 * @constructor Create empty S3file list listener callback
 */
interface S3FileListListenerCallback {
    /**
     * On list success
     *
     * @param onSuccess
     */
    fun onListSuccess(onSuccess: List<StorageItemResponse>)

    /**
     * On list failure
     *
     * @param onError
     */
    fun onListFailure(onError: String)
}