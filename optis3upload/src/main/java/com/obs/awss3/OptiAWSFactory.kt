package com.obs.awss3

import android.content.Context
import com.obs.awss3.core.S3FileDelete
import com.obs.awss3.core.S3FileDownload
import com.obs.awss3.core.S3FileList
import com.obs.awss3.core.S3FileUpload
import com.obs.awss3.listeners.*

/**
 * Exposed SDK api. OptiAWSFactory will act the initial entry point for
 * a session creation. Rest of the operation on the application side will be
 * based on the two interfaces returned.
 */
object OptiAWSFactory {
    fun createUploadAwsSession(
        events: S3UploadListenerCallback
    ): S3UploadSession {
        return S3FileUpload(events)
    }

    fun createDownloadAwsSession(
        events: S3DownloadListenerCallback
    ): S3DownloadSession {
        return S3FileDownload(events)
    }

    fun createRemoveAwsSession(
        events: S3RemoveListenerCallback
    ): S3RemoveSession {
        return S3FileDelete(events)
    }

    fun createFilesListAwsSession(
        events: S3FileListListenerCallback
    ): S3FileListSession {
        return S3FileList(events)
    }
}