package com.obs.awss3.core

import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.StorageItem
import com.amplifyframework.storage.options.StorageListOptions
import com.obs.awss3.listeners.S3FileListListener
import com.obs.awss3.model.StorageItemResponse

class S3FileList(private val s3FileListListener: S3FileListListener) {

    fun getFiles(path: String) {
        Amplify.Storage.list(path,
            { result ->
                s3FileListListener.onListSuccess(getStorageList(result.items))
            },
            { it.message?.let { s3FileListListener.onListFailure(it) } }
        )
    }


    fun getFiles(path: String, options: StorageListOptions) {
        Amplify.Storage.list(path,options,
            { result ->
                s3FileListListener.onListSuccess(getStorageList(result.items))
            },
            { it.message?.let { s3FileListListener.onListFailure(it) } }
        )
    }

    suspend fun getFilesCoroutines(path: String) {
        try {
            com.amplifyframework.kotlin.core.Amplify.Storage.list(path).let {
                s3FileListListener.onListSuccess(getStorageList(it.items))
            }
        } catch (error: StorageException) {
            error.message?.let { s3FileListListener.onListFailure(it) }
        }
    }

    suspend fun getFilesCoroutines(path: String, options: StorageListOptions) {
        try {
            com.amplifyframework.kotlin.core.Amplify.Storage.list(path,options).let {
                s3FileListListener.onListSuccess(getStorageList(it.items))
            }
        } catch (error: StorageException) {
            error.message?.let { s3FileListListener.onListFailure(it) }
        }
    }

    fun getStorageList(list: List<StorageItem>): List<StorageItemResponse>{
        var storageItemList:ArrayList<StorageItemResponse> = ArrayList()
        for(storage in list){
            storage.apply {
                storageItemList.add(StorageItemResponse(key,size,lastModified,eTag))
            }

        }
        return storageItemList
    }

}