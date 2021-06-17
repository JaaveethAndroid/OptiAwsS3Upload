package com.obs.sample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.obs.awss3.OptiAWSFactory
import com.obs.awss3.core.S3FileUpload
import com.obs.awss3.listeners.S3UploadListenerCallback
import com.obs.awss3.listeners.S3UploadSession
import com.obs.awss3.model.S3UploadErrorResponse
import com.obs.awss3.model.S3UploadProgessResponse
import com.obs.awss3.model.S3UploadFileResponse
import com.obs.awss3.model.S3UploadInputStreamResponse
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*


class MainActivity : AppCompatActivity(), S3UploadListenerCallback {

    private var mSelectedImageFileUri: Uri? = null
    private lateinit var s3UploadSession: S3UploadSession
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        s3UploadSession = OptiAWSFactory.createUploadAwsSession(this)



        button.setOnClickListener {
            // CHECK PERMISSIONS

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                showImageChooser(this@MainActivity)
            } else {
                /*Requests permissions to be granted to this application. These permissions
                 must be requested in your manifest, they should not be granted to your app,
                 and they should have protection level*/
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    3
                )
            }

        }

        GalleryButton.setOnClickListener {
            val intent = Intent(this@MainActivity, GalleryActivity::class.java)
            startActivity(intent)
        }


    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                if (data != null) {
                    try {

                        // The uri of selected image from phone storage.
                        mSelectedImageFileUri = data.data!!

                        val randomNumber = (1000..9999).random()
                      /*  GlobalScope.launch {
                            S3FileUpload(this@MainActivity).uploadInputStreamCoroutines(this@MainActivity,mSelectedImageFileUri!!,"UploadedFile" + randomNumber.toString(),this@MainActivity)
                        }*/

                        progressBar.visibility = View.VISIBLE
                        s3UploadSession.uploadInputStream(randomNumber.toString(),this,mSelectedImageFileUri!!,"UploadedFile" + randomNumber.toString())

                      // S3FileUpload(this@MainActivity).uploadInputStream(randomNumber.toString(),this,mSelectedImageFileUri!!,"UploadedFile" + randomNumber.toString())



                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@MainActivity,
                            "Image Selection Failed",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 3) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageChooser(this@MainActivity)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    "permission denied",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    fun showImageChooser(activity: Activity) {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        activity.startActivityForResult(galleryIntent, 2)
    }


    override fun onUploadSuccess(onSuccess: S3UploadInputStreamResponse) {
        progressBar.visibility = View.INVISIBLE
        Toast.makeText(this,"Successfully uploaded: ${onSuccess.key}",Toast.LENGTH_SHORT).show()
        val intent = Intent(this@MainActivity, GalleryActivity::class.java)
        startActivity(intent)
    }

    override fun OnUploadSuccess(onSuccess: S3UploadFileResponse) {
        progressBar.visibility = View.INVISIBLE
        Toast.makeText(this,"Successfully uploaded: ${onSuccess.key}",Toast.LENGTH_SHORT).show()
        val intent = Intent(this@MainActivity, GalleryActivity::class.java)
        startActivity(intent)
    }

    override fun onUploadFailure(onError: S3UploadErrorResponse) {
        progressBar.visibility = View.INVISIBLE
        Toast.makeText(this,"Upload failed ${onError.message}",Toast.LENGTH_SHORT).show()
    }

    override fun onUploadProgress(onProgress: S3UploadProgessResponse) {
        Log.i("MyAmplifyApp", "${onProgress.key} Upload progress = ${onProgress.progress}")
    }

}


