package com.obs.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.obs.awss3.OptiAWSFactory
import com.obs.awss3.core.S3FileDelete
import com.obs.sample.adapter.PictureAdapter
import com.obs.awss3.core.S3FileDownload
import com.obs.awss3.core.S3FileList
import com.obs.awss3.listeners.*
import com.obs.awss3.model.*
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.coroutines.*
import java.io.File


/**
 * Gallery activity
 *
 * @constructor Create empty Gallery activity
 */
class GalleryActivity : AppCompatActivity(), S3DownloadListenerCallback, S3FileListListenerCallback,
    S3RemoveListenerCallback {
    var filepaths = arrayListOf<S3File>()
    var files = arrayListOf<String>()
    private lateinit var s3DownloadSession: S3DownloadSession
    private lateinit var s3RemoveSession: S3RemoveSession
    private lateinit var s3FileListSession: S3FileListSession
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        s3DownloadSession = OptiAWSFactory.createDownloadAwsSession(this)
        s3RemoveSession = OptiAWSFactory.createRemoveAwsSession(this)
        s3FileListSession = OptiAWSFactory.createFilesListAwsSession(this)

        progressBar.visibility = View.VISIBLE
        s3FileListSession.getFiles("")

    }

    /**
     * Deletefiles
     *
     * @param file
     * @param pos
     * @param list
     */
    fun deletefiles(file: S3File, pos: Int, list: ArrayList<S3File>){
        println(file)
        println(pos)

      s3RemoveSession.deleteFile(pos.toString(),file.origin)

        }


    /**
     * Populaterv
     *
     * @param list
     */
    fun populaterv(list: ArrayList<S3File>){
        Log.d("downloadlist", list.toString())

        if(progressBar.visibility == View.VISIBLE){
            progressBar.visibility = View.GONE
        }


        FileRecyclerView.apply {
            layoutManager = GridLayoutManager(this@GalleryActivity,3)
            adapter = PictureAdapter(this@GalleryActivity, list, this@GalleryActivity)
            (adapter as PictureAdapter).notifyDataSetChanged()
        }
    }

    /**
     * Downloadprogress
     *
     * @param file
     */
    fun downloadprogress(file: String){
        /*FileNameTV.visibility = View.VISIBLE
        FileNameTV.setText("${file} Downloaded")*/

    }

    private  fun downloadfiles(file: ArrayList<String>)  {

        val downloadFolder = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

           filepaths = arrayListOf<S3File>()

            file.forEach { item-> val randomNumber = (1000..9999).random()
                Thread.sleep(500)

                s3DownloadSession.generateURL(randomNumber.toString(),item)

                s3DownloadSession.downloadFile(randomNumber.toString(),File("$downloadFolder/$item.jpg"),item)

            }


    }

    override fun onDownloadSuccess(onSuccess: S3DownloadFileResponse) {
        Log.d("MyAmplifyApp", "Successfully downloaded: ${onSuccess.file.name} Path: ${onSuccess.file.absolutePath}")
        downloadprogress(onSuccess.file.name)

        val fileobj = S3File(
            path = onSuccess.file.absolutePath,
            key = onSuccess.file.name, // downloaded filename
            origin = onSuccess.key, //original filename
        )
        filepaths.add(fileobj)

        populaterv(filepaths)
    }

    override fun onDownloadSuccess(onSuccess: S3DownloadURLResponse) {
        Log.i("MyAmplifyApp", "Successfully generated: ${onSuccess.url}")
        progressBar.visibility = View.INVISIBLE
    }

    override fun onDownloadFailure(onError: S3DownloadErrorResponse) {
        Log.d("MyAmplifyApp", "Download Failure: ${onError.message}")
        progressBar.visibility = View.INVISIBLE
    }


    override fun onDownloadProgress(onProgress: S3DownloadProgessResponse) {
        Log.d("MyAmplifyApp", "Fraction completed: ${onProgress.progress}")
        progressBar.visibility = View.INVISIBLE
    }

    override fun onRemoveSuccess(onSuccess: S3RemoveFileResponse) {
        Toast.makeText(this,"Successfully Removed", Toast.LENGTH_SHORT).show()
        S3FileList(this).getFiles("")
    }

    override fun onRemoveError(onError: S3RemoveFileErrorResponse) {
        Log.d("MyAmplifyApp", onError.message)
        Toast.makeText(this,onError.message, Toast.LENGTH_SHORT).show()
    }

    override fun onListSuccess(onSuccess: List<StorageItemResponse>) {
        GlobalScope.launch(Dispatchers.IO) {
            val filesTxt = arrayListOf<String>()
            files = arrayListOf<String>()
            onSuccess.forEach { item ->
                Log.d("MyAmplifyApp", "Item: " + item.key)
                filesTxt += item.key + " ${(item.size).div(1000)}KB"
                files.add(item.key)
            }
            withContext(Dispatchers.Main) {
                progressBar.visibility = View.VISIBLE
                GlobalScope.launch(Dispatchers.IO) {
                    downloadfiles(files)
                }
            }
        }
    }

    override fun onListFailure(onError: String) {
        progressBar.visibility = View.INVISIBLE
        Toast.makeText(this,onError, Toast.LENGTH_SHORT).show()
        Log.d("MyAmplifyApp", "List Failure "+onError)
    }
}