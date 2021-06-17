package com.obs.awss3.core

import androidx.lifecycle.LifecycleOwner
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.StorageException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import androidx.lifecycle.lifecycleScope
import com.amplifyframework.storage.options.StorageDownloadFileOptions
import com.obs.awss3.listeners.S3DownloadListenerCallback
import com.obs.awss3.listeners.S3DownloadSession
import com.obs.awss3.model.S3DownloadErrorResponse
import com.obs.awss3.model.S3DownloadFileResponse
import com.obs.awss3.model.S3DownloadProgessResponse
import com.obs.awss3.model.S3DownloadURLResponse
import java.io.File

class S3FileDownload(private val s3DownloadListener: S3DownloadListenerCallback): S3DownloadSession {


    override fun downloadFile(id: String, file: File, key: String) {
        val options = StorageDownloadFileOptions.defaultInstance()
        Amplify.Storage.downloadFile(key, file, options,
            { s3DownloadListener.onDownloadProgress(S3DownloadProgessResponse(id,key,it.fractionCompleted)) },
            { s3DownloadListener.onDownloadSuccess(S3DownloadFileResponse(id,it.file,key)) },
            { it.message?.let { s3DownloadListener.onDownloadFailure(S3DownloadErrorResponse(id,it)) } }
        )
    }

    override fun downloadFile(id: String,file: File, key: String,options: StorageDownloadFileOptions) {
        Amplify.Storage.downloadFile(key, file, options,
            { s3DownloadListener.onDownloadProgress(S3DownloadProgessResponse(id,key,it.fractionCompleted)) },
            { s3DownloadListener.onDownloadSuccess(S3DownloadFileResponse(id,it.file,key)) },
            { it.message?.let { s3DownloadListener.onDownloadFailure(S3DownloadErrorResponse(id,it)) } }
        )
    }

    override suspend fun downloadFileCoroutines(id: String,file: File, key: String,lifecycleOwner: LifecycleOwner) {
        val options = StorageDownloadFileOptions.defaultInstance()
        val download = com.amplifyframework.kotlin.core.Amplify.Storage.downloadFile(key, file, options)
        val progressJob = lifecycleOwner.lifecycleScope.async {
            download.progress().collect { progress ->
                s3DownloadListener.onDownloadProgress(S3DownloadProgessResponse(id,key,progress.fractionCompleted))
            }
        }
        try {
            s3DownloadListener.onDownloadSuccess(S3DownloadFileResponse(id,download.result().file,key))
        } catch (error: StorageException) {
            error.message?.let { s3DownloadListener.onDownloadFailure(S3DownloadErrorResponse(id,it)) }
        }
        progressJob.cancel()
    }

    override suspend fun downloadFileCoroutines(id: String,file: File, options: StorageDownloadFileOptions,key: String,lifecycleOwner: LifecycleOwner) {
        val download = com.amplifyframework.kotlin.core.Amplify.Storage.downloadFile(key, file, options)
        val progressJob = lifecycleOwner.lifecycleScope.async {
            download.progress().collect { progress ->
                s3DownloadListener.onDownloadProgress(S3DownloadProgessResponse(id,key,progress.fractionCompleted))
            }
        }
        try {
            s3DownloadListener.onDownloadSuccess(S3DownloadFileResponse(id,download.result().file,key))
        } catch (error: StorageException) {
            error.message?.let { s3DownloadListener.onDownloadFailure(S3DownloadErrorResponse(id,it)) }
        }
        progressJob.cancel()
    }

    override fun generateURL(id: String, key: String) {
        Amplify.Storage.getUrl(
            key,
            {  s3DownloadListener.onDownloadSuccess(S3DownloadURLResponse(id,it.url)) },
            {  it.message?.let { s3DownloadListener.onDownloadFailure(S3DownloadErrorResponse(id,it)) } }
        )
    }

    override suspend fun generateURLCoroutines(id: String, key: String) {
        try {
            val url = com.amplifyframework.kotlin.core.Amplify.Storage.getUrl(key)
            s3DownloadListener.onDownloadSuccess(S3DownloadURLResponse(id,url.url))
        } catch (error: StorageException) {
            error.message?.let { s3DownloadListener.onDownloadFailure(S3DownloadErrorResponse(id,it)) }
        }
    }

}