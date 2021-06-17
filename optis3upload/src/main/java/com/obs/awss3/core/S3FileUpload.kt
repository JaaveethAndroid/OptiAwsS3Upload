package com.obs.awss3.core

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.options.StorageUploadInputStreamOptions
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import androidx.lifecycle.lifecycleScope
import com.amplifyframework.storage.options.StorageUploadFileOptions
import com.obs.awss3.listeners.S3UploadListenerCallback
import com.obs.awss3.listeners.S3UploadSession
import com.obs.awss3.model.S3UploadErrorResponse
import com.obs.awss3.model.S3UploadProgessResponse
import com.obs.awss3.model.S3UploadFileResponse
import com.obs.awss3.model.S3UploadInputStreamResponse
import java.io.File

class S3FileUpload(private val s3UploadListener: S3UploadListenerCallback): S3UploadSession {


     override fun uploadInputStream(id: String, activity: Activity, Uri: Uri, keyname: String) {
        val exampleInputStream = activity.getContentResolver().openInputStream(Uri)
        exampleInputStream?.let {
            Amplify.Storage.uploadInputStream(
                keyname,
                it,
                StorageUploadInputStreamOptions.defaultInstance(),
                {
                    s3UploadListener?.onUploadProgress(S3UploadProgessResponse(id,keyname,it.fractionCompleted))
                },
                { result ->
                    s3UploadListener?.onUploadSuccess(S3UploadInputStreamResponse(id,result.key))
                },
                { error -> error.message?.let { s3UploadListener?.onUploadFailure(
                    S3UploadErrorResponse(id,it)
                ) } }
            )
        }

    }

    override fun uploadInputStream(id: String,activity: Activity, Uri: Uri, keyname: String, options: StorageUploadInputStreamOptions) {
        val exampleInputStream = activity.getContentResolver().openInputStream(Uri)
        exampleInputStream?.let {
            Amplify.Storage.uploadInputStream(
                keyname,
                it,
                options,
                {
                    s3UploadListener?.onUploadProgress(S3UploadProgessResponse(id,keyname,it.fractionCompleted))
                },
                { result ->
                    s3UploadListener?.onUploadSuccess(S3UploadInputStreamResponse(id,result.key))
                },
                { error -> error.message?.let { s3UploadListener?.onUploadFailure( S3UploadErrorResponse(id,it)) } }
            )
        }

    }

    override fun uploadFile(id: String,activity: Activity, file: File, keyname: String) {
        Amplify.Storage.uploadFile(
            keyname,
            file,
            StorageUploadFileOptions.defaultInstance(),
            {
                s3UploadListener?.onUploadProgress(S3UploadProgessResponse(id,keyname,it.fractionCompleted))
            },
            { result ->
                s3UploadListener?.OnUploadSuccess(S3UploadFileResponse(id,result.key))
            },
            { error -> error.message?.let { s3UploadListener?.onUploadFailure( S3UploadErrorResponse(id,it)) } })
    }

    override fun uploadFile(id: String,activity: Activity, file: File, keyname: String, options:StorageUploadFileOptions) {
        Amplify.Storage.uploadFile(
            keyname,
            file,
            options,
            {
                s3UploadListener?.onUploadProgress(S3UploadProgessResponse(id,keyname,it.fractionCompleted))
            },
            { result ->
                s3UploadListener?.OnUploadSuccess(S3UploadFileResponse(id,result.key))
            },
            { error -> error.message?.let { s3UploadListener?.onUploadFailure( S3UploadErrorResponse(id,it)) } })
    }

    override suspend fun uploadInputStreamCoroutines(
        id: String,
        activity: Activity, uri: Uri,
        keyname: String, lifecycleOwner: LifecycleOwner,
        options: StorageUploadInputStreamOptions
    ) {
        val stream = activity.contentResolver.openInputStream(uri)
        stream?.let {
            val upload = com.amplifyframework.kotlin.core.Amplify.Storage.uploadInputStream(keyname, it,options)
            lifecycleOwner.lifecycleScope.async {
                upload
                    .progress()
                    .collect {   s3UploadListener?.onUploadProgress(S3UploadProgessResponse(id,keyname,it.fractionCompleted)) }
            }
            lifecycleOwner.lifecycleScope.async {
                try {
                    val result = upload.result()
                    s3UploadListener?.onUploadSuccess(S3UploadInputStreamResponse(id,result.key))
                } catch (error: StorageException) {
                    error.message?.let { s3UploadListener?.onUploadFailure( S3UploadErrorResponse(id,it)) }
                }
            }

        }

    }

    override suspend fun uploadInputStreamCoroutines(
        id: String,
        activity: Activity, uri: Uri,
        keyname: String, lifecycleOwner: LifecycleOwner
    ) {
        val stream = activity.contentResolver.openInputStream(uri)
        stream?.let {
            val upload = com.amplifyframework.kotlin.core.Amplify.Storage.uploadInputStream(keyname, it)
            lifecycleOwner.lifecycleScope.async {
                upload
                    .progress()
                    .collect {   s3UploadListener?.onUploadProgress(S3UploadProgessResponse(id,keyname,it.fractionCompleted)) }
            }
            lifecycleOwner.lifecycleScope.async {
                try {
                    val result = upload.result()
                    s3UploadListener?.onUploadSuccess(S3UploadInputStreamResponse(id,result.key))
                } catch (error: StorageException) {
                    error.message?.let { s3UploadListener?.onUploadFailure( S3UploadErrorResponse(id,it)) }
                }
            }

        }

    }

    override suspend fun uploadFileCoroutines(id: String,activity: Activity, file: File, key: String,lifecycleOwner: LifecycleOwner) {
        val options = StorageUploadFileOptions.defaultInstance()
        val upload = com.amplifyframework.kotlin.core.Amplify.Storage.uploadFile(key, file, options)
        val progressJob = lifecycleOwner.lifecycleScope.async {
            upload.progress().collect {
                s3UploadListener?.onUploadProgress(S3UploadProgessResponse(id,key,it.fractionCompleted))
            }
        }
        try {
            val result = upload.result()
            s3UploadListener?.OnUploadSuccess(S3UploadFileResponse(id,result.key))
        } catch (error: StorageException) {
            error.message?.let { s3UploadListener?.onUploadFailure( S3UploadErrorResponse(id,it)) }
        }
        progressJob.cancel()
    }

    override suspend fun uploadFileCoroutines(id: String,activity: Activity, file: File, key: String,lifecycleOwner: LifecycleOwner,options: StorageUploadFileOptions) {
        val upload = com.amplifyframework.kotlin.core.Amplify.Storage.uploadFile(key, file, options)
        val progressJob = lifecycleOwner.lifecycleScope.async {
            upload.progress().collect {
                s3UploadListener?.onUploadProgress(S3UploadProgessResponse(id,key,it.fractionCompleted))
            }
        }
        try {
            val result = upload.result()
            s3UploadListener?.OnUploadSuccess(S3UploadFileResponse(id,result.key))
        } catch (error: StorageException) {
            error.message?.let { s3UploadListener?.onUploadFailure( S3UploadErrorResponse(id,it)) }
        }
        progressJob.cancel()
    }
}