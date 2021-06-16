package com.obs.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.obs.awss3.core.S3FileDelete
import com.obs.sample.adapter.PictureAdapter
import com.obs.awss3.listeners.S3DownloadListener
import com.obs.awss3.core.S3FileDownload
import com.obs.awss3.core.S3FileList
import com.obs.awss3.listeners.S3FileListListener
import com.obs.awss3.listeners.S3RemoveListener
import com.obs.awss3.model.*
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.coroutines.*
import java.io.File


class GalleryActivity : AppCompatActivity(), S3DownloadListener, S3FileListListener,
    S3RemoveListener {
    var filepaths = arrayListOf<S3File>()
    var files = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)


        S3FileList(this).getFiles("")

    }

  fun deletefiles(file: S3File, pos: Int, list: ArrayList<S3File>){
        println(file)
        println(pos)

      S3FileDelete(this).deleteFile(pos.toString(),file.origin)

        }






    fun populaterv(list: ArrayList<S3File>){
        Log.d("downloadlist", list.toString())
        if (FileNameTV.visibility == View.VISIBLE){
            FileNameTV.visibility = View.GONE
        }

        if(progressBar.visibility == View.VISIBLE){
            progressBar.visibility = View.GONE
        }


        FileRecyclerView.apply {
            layoutManager = GridLayoutManager(this@GalleryActivity,3)
            adapter = PictureAdapter(this@GalleryActivity, list, this@GalleryActivity)
            (adapter as PictureAdapter).notifyDataSetChanged()
        }
    }

    fun downloadprogress(file: String){
        FileNameTV.visibility = View.VISIBLE
        FileNameTV.setText("${file} Downloaded")

    }

    private  fun downloadfiles(file: ArrayList<String>)  {

        val downloadFolder = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

           filepaths = arrayListOf<S3File>()

            file.forEach { item-> val randomNumber = (1000..9999).random()
                Thread.sleep(500)

                S3FileDownload(this).generateURL(randomNumber.toString(),item)

                S3FileDownload(this).downloadFile(randomNumber.toString(),File("$downloadFolder/$item.jpg"),item)

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
    }

    override fun onDownloadFailure(onError: S3DownloadErrorResponse) {
        Log.d("MyAmplifyApp", "Download Failure: ${onError.message}")
    }


    override fun onDownloadProgress(onProgress: S3DownloadProgessResponse) {
        Log.d("MyAmplifyApp", "Fraction completed: ${onProgress.progress}")
    }

    override fun onRemoveSuccess(onSuccess: S3RemoveFileResponse) {
        S3FileList(this).getFiles("")
    }

    override fun onRemoveError(onError: S3RemoveFileErrorResponse) {
        Log.d("MyAmplifyApp", onError.message)
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
                FileListTV.setText((filesTxt.toString()).replace("]", "").replace("[", ""))
                FileListTV.visibility = View.VISIBLE

                FileDwnBtn.visibility = View.VISIBLE

                FileDwnBtn.setOnClickListener {
                    progressBar.visibility = View.VISIBLE
                    GlobalScope.launch(Dispatchers.IO) {
                        downloadfiles(files)
                    }

                }
            }
        }
    }

    override fun onListFailure(onError: String) {
        Log.d("MyAmplifyApp", "List Failure "+onError)
    }
}