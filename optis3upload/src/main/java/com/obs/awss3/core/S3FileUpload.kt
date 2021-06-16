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
import com.obs.awss3.listeners.S3UploadListener
import java.io.File

class S3FileUpload(private val s3UploadListener: S3UploadListener) {


     fun uploadInputStream(activity: Activity, Uri: Uri, keyname: String) {
        val exampleInputStream = activity.getContentResolver().openInputStream(Uri)
        exampleInputStream?.let {
            Amplify.Storage.uploadInputStream(
                keyname,
                it,
                StorageUploadInputStreamOptions.defaultInstance(),
                {
                    s3UploadListener?.onProgress(it)
                },
                { result ->
                    s3UploadListener?.onSuccess(result)
                },
                { error -> s3UploadListener?.onFailure(error) }
            )
        }

    }

    fun uploadInputStream(activity: Activity, Uri: Uri, keyname: String, options: StorageUploadInputStreamOptions) {
        val exampleInputStream = activity.getContentResolver().openInputStream(Uri)
        exampleInputStream?.let {
            Amplify.Storage.uploadInputStream(
                keyname,
                it,
                options,
                {
                    s3UploadListener?.onProgress(it)
                },
                { result ->
                    s3UploadListener?.onSuccess(result)
                },
                { error -> s3UploadListener?.onFailure(error) }
            )
        }

    }

    fun uploadFile(activity: Activity, file: File, keyname: String) {
        Amplify.Storage.uploadFile(
            keyname,
            file,
            StorageUploadFileOptions.defaultInstance(),
            {
                s3UploadListener?.onProgress(it)
            },
            { result ->
                s3UploadListener?.OnSuccess(result)
            },
            { error -> s3UploadListener?.onFailure(error) })
    }

    suspend fun uploadInputStreamCoroutines(
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
                    .collect {   s3UploadListener?.onProgress(it) }
            }
            lifecycleOwner.lifecycleScope.async {
                try {
                    val result = upload.result()
                    s3UploadListener?.onSuccess(result)
                } catch (error: StorageException) {
                    s3UploadListener?.onFailure(error)
                }
            }

        }

    }

    suspend fun uploadInputStreamCoroutines(
        activity: Activity, uri: Uri,
        keyname: String, lifecycleOwner: LifecycleOwner
    ) {
        val stream = activity.contentResolver.openInputStream(uri)
        stream?.let {
            val upload = com.amplifyframework.kotlin.core.Amplify.Storage.uploadInputStream(keyname, it)
            lifecycleOwner.lifecycleScope.async {
                upload
                    .progress()
                    .collect {   s3UploadListener?.onProgress(it) }
            }
            lifecycleOwner.lifecycleScope.async {
                try {
                    val result = upload.result()
                    s3UploadListener?.onSuccess(result)
                } catch (error: StorageException) {
                    s3UploadListener?.onFailure(error)
                }
            }

        }

    }
}