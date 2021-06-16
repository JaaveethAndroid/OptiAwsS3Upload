package com.obs.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.StorageException
import com.amplifyframework.storage.result.StorageDownloadFileResult
import com.amplifyframework.storage.result.StorageGetUrlResult
import com.amplifyframework.storage.result.StorageListResult
import com.amplifyframework.storage.result.StorageTransferProgress
import com.obs.sample.adapter.PictureAdapter
import com.obs.awss3.listeners.S3DownloadListener
import com.obs.awss3.core.S3FileDownload
import com.obs.awss3.core.S3FileList
import com.obs.awss3.listeners.S3FileListListener
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.coroutines.*
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class GalleryActivity : AppCompatActivity(), S3DownloadListener, S3FileListListener {
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
        Log.d("predelete", list.toString())

      suspend fun delfile(): String {
          return suspendCoroutine { continuation ->
              Amplify.Storage.remove(
                  file.origin,
                  { result -> Log.d("MyAmplifyApp", "Successfully removed: " + result.getKey())
                      continuation.resume("success")


                  },
                  { error -> Log.e("MyAmplifyApp", "Remove failure", error)

                  }
              )
          }
      }

      GlobalScope.launch(Dispatchers.IO){
          val del = delfile()
          if(del == "success"){
              Log.d("delete", del)
              withContext(Dispatchers.Main){
                  list.removeAt(pos)
                  populaterv(list)
              }
          }
      }


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

                S3FileDownload(this).generateURL(item)

                S3FileDownload(this).downloadFile(File("$downloadFolder/$item.jpg"),item)

            }


    }

    override fun onSuccess(onSuccess: StorageDownloadFileResult) {
        Log.d("MyAmplifyApp", "Successfully downloaded: ${onSuccess.getFile().name} Path: ${onSuccess.file.absolutePath}")
        downloadprogress(onSuccess.getFile().name)

        val fileobj = S3File(
            path = onSuccess.file.absolutePath,
            key = onSuccess.file.name, // downloaded filename
            origin = onSuccess.file.name, //original filename
        )
        filepaths.add(fileobj)

        populaterv(filepaths)
        /*println("filelists${filepaths.size} ${onSuccess.file.size}")
        if(filepaths.size == file.size){
            populaterv(filepaths)
        }*/
    }

    override fun onSuccess(onSuccess: StorageGetUrlResult) {
        Log.i("MyAmplifyApp", "Successfully generated: ${onSuccess.url}")
    }

    override fun onSuccess(onSuccess: StorageListResult) {
        GlobalScope.launch(Dispatchers.IO) {
            val filesTxt = arrayListOf<String>()
            files = arrayListOf<String>()
            onSuccess.getItems().forEach { item ->
                Log.d("MyAmplifyApp", "Item: " + item.getKey())
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

    override fun onFailure(onError: StorageException) {
        Log.d("MyAmplifyApp", "Download Failure", onError)
    }

    override fun onProgress(onProgress: StorageTransferProgress) {
        Log.d("MyAmplifyApp", "Fraction completed: ${onProgress.fractionCompleted}")
    }
}