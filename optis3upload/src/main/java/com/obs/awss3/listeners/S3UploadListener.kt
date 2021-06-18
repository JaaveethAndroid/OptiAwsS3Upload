package com.obs.awss3.listeners

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import com.amplifyframework.storage.options.StorageUploadFileOptions
import com.amplifyframework.storage.options.StorageUploadInputStreamOptions
import com.obs.awss3.model.S3UploadErrorResponse
import com.obs.awss3.model.S3UploadProgessResponse
import com.obs.awss3.model.S3UploadFileResponse
import com.obs.awss3.model.S3UploadInputStreamResponse
import java.io.File


/**
 * S3upload session
 *
 * @constructor Create empty S3upload session
 */
interface S3UploadSession {
    /**
     * Upload input stream
     *
     * @param id
     * @param activity
     * @param Uri
     * @param keyname
     */
    fun uploadInputStream(id: String, activity: Activity, Uri: Uri, keyname: String)

    /**
     * Upload input stream
     *
     * @param id
     * @param activity
     * @param Uri
     * @param keyname
     * @param options
     */
    fun uploadInputStream(id: String, activity: Activity, Uri: Uri, keyname: String, options: StorageUploadInputStreamOptions)

    /**
     * Upload file
     *
     * @param id
     * @param activity
     * @param file
     * @param keyname
     */
    fun uploadFile(id: String, activity: Activity, file: File, keyname: String)

    /**
     * Upload file
     *
     * @param id
     * @param activity
     * @param file
     * @param keyname
     * @param options
     */
    fun uploadFile(id: String, activity: Activity, file: File, keyname: String, options: StorageUploadFileOptions)

    /**
     * Upload input stream coroutines
     *
     * @param id
     * @param activity
     * @param uri
     * @param keyname
     * @param lifecycleOwner
     * @param options
     */
    suspend fun uploadInputStreamCoroutines(
        id: String,
        activity: Activity, uri: Uri,
        keyname: String, lifecycleOwner: LifecycleOwner,
        options: StorageUploadInputStreamOptions
    )

    /**
     * Upload input stream coroutines
     *
     * @param id
     * @param activity
     * @param uri
     * @param keyname
     * @param lifecycleOwner
     */
    suspend fun uploadInputStreamCoroutines(
        id: String,
        activity: Activity, uri: Uri,
        keyname: String, lifecycleOwner: LifecycleOwner
    )

    /**
     * Upload file coroutines
     *
     * @param id
     * @param activity
     * @param file
     * @param key
     * @param lifecycleOwner
     */
    suspend fun uploadFileCoroutines(id: String, activity: Activity, file: File, key: String, lifecycleOwner: LifecycleOwner)

    /**
     * Upload file coroutines
     *
     * @param id
     * @param activity
     * @param file
     * @param key
     * @param lifecycleOwner
     * @param options
     */
    suspend fun uploadFileCoroutines(id: String, activity: Activity, file: File, key: String, lifecycleOwner: LifecycleOwner, options: StorageUploadFileOptions)
}

/**
 * S3upload listener callback
 *
 * @constructor Create empty S3upload listener callback
 */
interface S3UploadListenerCallback {
    /**
     * On upload success
     *
     * @param onSuccess
     */
    fun onUploadSuccess(onSuccess: S3UploadInputStreamResponse)

    /**
     * On upload success
     *
     * @param onSuccess
     */
    fun OnUploadSuccess(onSuccess: S3UploadFileResponse)

    /**
     * On upload failure
     *
     * @param onError
     */
    fun onUploadFailure(onError: S3UploadErrorResponse)

    /**
     * On upload progress
     *
     * @param onProgress
     */
    fun onUploadProgress(onProgress: S3UploadProgessResponse)
}