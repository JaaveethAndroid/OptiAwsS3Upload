package com.obs.awss3.core

import androidx.lifecycle.LifecycleOwner
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.StorageException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import androidx.lifecycle.lifecycleScope
import com.amplifyframework.storage.options.StorageDownloadFileOptions
import com.obs.awss3.listeners.S3DownloadListener
import java.io.File

class S3FileDownload(private val s3DownloadListener: S3DownloadListener) {


    fun downloadFile(file: File, key: String) {
        val options = StorageDownloadFileOptions.defaultInstance()
        Amplify.Storage.downloadFile(key, file, options,
            { s3DownloadListener.onProgress(it) },
            { s3DownloadListener.onSuccess(it) },
            { s3DownloadListener.onFailure(it) }
        )
    }

    fun downloadFile(file: File, key: String,options: StorageDownloadFileOptions) {
        Amplify.Storage.downloadFile(key, file, options,
            { s3DownloadListener.onProgress(it) },
            { s3DownloadListener.onSuccess(it) },
            { s3DownloadListener.onFailure(it) }
        )
    }

    suspend fun downloadFileCoroutines(file: File, key: String,lifecycleOwner: LifecycleOwner) {
        val options = StorageDownloadFileOptions.defaultInstance()
        val download = com.amplifyframework.kotlin.core.Amplify.Storage.downloadFile(key, file, options)
        val progressJob = lifecycleOwner.lifecycleScope.async {
            download.progress().collect { progress ->
                s3DownloadListener.onProgress(progress)
            }
        }
        try {
            s3DownloadListener.onSuccess(download.result())
        } catch (error: StorageException) {
            s3DownloadListener.onFailure(error)
        }
        progressJob.cancel()
    }

    suspend fun downloadFileCoroutines(file: File, options: StorageDownloadFileOptions,key: String,lifecycleOwner: LifecycleOwner) {
        val download = com.amplifyframework.kotlin.core.Amplify.Storage.downloadFile(key, file, options)
        val progressJob = lifecycleOwner.lifecycleScope.async {
            download.progress().collect { progress ->
                s3DownloadListener.onProgress(progress)
            }
        }
        try {
            s3DownloadListener.onSuccess(download.result())
        } catch (error: StorageException) {
            s3DownloadListener.onFailure(error)
        }
        progressJob.cancel()
    }

    fun generateURL( key: String) {
        Amplify.Storage.getUrl(
            key,
            {  s3DownloadListener.onSuccess(it) },
            {  s3DownloadListener.onFailure(it) }
        )
    }

    suspend fun generateURLCoroutines( key: String) {
        try {
            val url = com.amplifyframework.kotlin.core.Amplify.Storage.getUrl(key)
            s3DownloadListener.onSuccess(url)
        } catch (error: StorageException) {
            s3DownloadListener.onFailure(error)
        }
    }

}