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

interface S3DownloadSession {
    fun downloadFile(id: String, file: File, key: String)
    fun downloadFile(id: String,file: File, key: String,options: StorageDownloadFileOptions)
    suspend fun downloadFileCoroutines(id: String,file: File, key: String,lifecycleOwner: LifecycleOwner)
    suspend fun downloadFileCoroutines(id: String,file: File, options: StorageDownloadFileOptions,key: String,lifecycleOwner: LifecycleOwner)
    fun generateURL(id: String, key: String)
    suspend fun generateURLCoroutines(id: String, key: String)
}
interface S3DownloadListenerCallback {
    fun onDownloadSuccess(onSuccess: S3DownloadFileResponse)
    fun onDownloadSuccess(onSuccess: S3DownloadURLResponse)
    fun onDownloadFailure(onError: S3DownloadErrorResponse)
    fun onDownloadProgress(onProgress: S3DownloadProgessResponse)
}