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


interface S3UploadSession {
    fun uploadInputStream(id: String, activity: Activity, Uri: Uri, keyname: String)
    fun uploadInputStream(id: String,activity: Activity, Uri: Uri, keyname: String, options: StorageUploadInputStreamOptions)
    fun uploadFile(id: String, activity: Activity, file: File, keyname: String)
    fun uploadFile(id: String,activity: Activity, file: File, keyname: String, options: StorageUploadFileOptions)
    suspend fun uploadInputStreamCoroutines(
        id: String,
        activity: Activity, uri: Uri,
        keyname: String, lifecycleOwner: LifecycleOwner,
        options: StorageUploadInputStreamOptions
    )
    suspend fun uploadInputStreamCoroutines(
        id: String,
        activity: Activity, uri: Uri,
        keyname: String, lifecycleOwner: LifecycleOwner
    )
    suspend fun uploadFileCoroutines(id: String,activity: Activity, file: File, key: String,lifecycleOwner: LifecycleOwner)
    suspend fun uploadFileCoroutines(id: String,activity: Activity, file: File, key: String,lifecycleOwner: LifecycleOwner,options: StorageUploadFileOptions)
}

interface S3UploadListenerCallback {
    fun onUploadSuccess(onSuccess: S3UploadInputStreamResponse)
    fun OnUploadSuccess(onSuccess: S3UploadFileResponse)
    fun onUploadFailure(onError: S3UploadErrorResponse)
    fun onUploadProgress(onProgress: S3UploadProgessResponse)
}