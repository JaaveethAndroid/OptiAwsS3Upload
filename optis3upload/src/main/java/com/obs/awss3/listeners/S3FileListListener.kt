package com.obs.awss3.listeners

import com.obs.awss3.model.StorageItemResponse


interface S3FileListListener {
    fun onListSuccess(onSuccess: List<StorageItemResponse>)
    fun onListFailure(onError: String)
}