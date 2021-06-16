package com.obs.sample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.obs.awss3.listeners.S3UploadListener
import com.obs.awss3.core.S3FileUpload
import com.obs.awss3.model.S3UploadErrorResponse
import com.obs.awss3.model.S3UploadProgessResponse
import com.obs.awss3.model.S3UploadFileResponse
import com.obs.awss3.model.S3UploadInputStreamResponse
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*


class MainActivity : AppCompatActivity(), S3UploadListener {

    private var mSelectedImageFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

                       S3FileUpload(this@MainActivity).uploadInputStream(randomNumber.toString(),this,mSelectedImageFileUri!!,"UploadedFile" + randomNumber.toString())



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
        Log.i("MyAmplifyApp", "Successfully uploaded: ${onSuccess.key}.")
    }

    override fun OnUploadSuccess(onSuccess: S3UploadFileResponse) {
        Log.i("MyAmplifyApp", "Successfully uploaded: ${onSuccess.key}.")
    }

    override fun onUploadFailure(onError: S3UploadErrorResponse) {
        Log.e("MyAmplifyApp", "Upload failed ${onError}")
    }

    override fun onUploadProgress(onProgress: S3UploadProgessResponse) {
        Log.i("MyAmplifyApp", "${onProgress.key} Upload progress = ${onProgress.progress}")
    }

}


