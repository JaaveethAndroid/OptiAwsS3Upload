package com.obs.awss3.listeners

import com.obs.awss3.model.S3UploadErrorResponse
import com.obs.awss3.model.S3UploadProgessResponse
import com.obs.awss3.model.S3UploadFileResponse
import com.obs.awss3.model.S3UploadInputStreamResponse


interface S3UploadListener {
    fun onUploadSuccess(onSuccess: S3UploadInputStreamResponse)
    fun OnUploadSuccess(onSuccess: S3UploadFileResponse)
    fun onUploadFailure(onError: S3UploadErrorResponse)
    fun onUploadProgress(onProgress: S3UploadProgessResponse)
}