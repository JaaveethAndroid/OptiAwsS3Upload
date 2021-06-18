package com.obs.awss3.listeners

import androidx.lifecycle.LifecycleOwner
import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.options.StorageDownloadFileOptions
import com.amplifyframework.storage.result.*
import com.obs.awss3.model.S3DownloadErrorResponse
import com.obs.awss3.model.S3DownloadFileResponse
import com.obs.awss3.model.S3DownloadProgessResponse
import com.obs.awss3.model.S3DownloadURLResponse
import java.io.File

/**
 * S3download session
 *
 * @constructor Create empty S3download session
 */
interface S3DownloadSession {
    /**
     * Download file
     *
     * @param id
     * @param file
     * @param key
     */
    fun downloadFile(id: String, file: File, key: String)

    /**
     * Download file
     *
     * @param id
     * @param file
     * @param key
     * @param options
     */
    fun downloadFile(id: String, file: File, key: String, options: StorageDownloadFileOptions)

    /**
     * Download file coroutines
     *
     * @param id
     * @param file
     * @param key
     * @param lifecycleOwner
     */
    suspend fun downloadFileCoroutines(id: String, file: File, key: String, lifecycleOwner: LifecycleOwner)

    /**
     * Download file coroutines
     *
     * @param id
     * @param file
     * @param options
     * @param key
     * @param lifecycleOwner
     */
    suspend fun downloadFileCoroutines(id: String, file: File, options: StorageDownloadFileOptions, key: String, lifecycleOwner: LifecycleOwner)

    /**
     * Generate u r l
     *
     * @param id
     * @param key
     */
    fun generateURL(id: String, key: String)

    /**
     * Generate u r l coroutines
     *
     * @param id
     * @param key
     */
    suspend fun generateURLCoroutines(id: String, key: String)
}

/**
 * S3download listener callback
 *
 * @constructor Create empty S3download listener callback
 */
interface S3DownloadListenerCallback {
    /**
     * On download success
     *
     * @param onSuccess
     */
    fun onDownloadSuccess(onSuccess: S3DownloadFileResponse)

    /**
     * On download success
     *
     * @param onSuccess
     */
    fun onDownloadSuccess(onSuccess: S3DownloadURLResponse)

    /**
     * On download failure
     *
     * @param onError
     */
    fun onDownloadFailure(onError: S3DownloadErrorResponse)

    /**
     * On download progress
     *
     * @param onProgress
     */
    fun onDownloadProgress(onProgress: S3DownloadProgessResponse)
}