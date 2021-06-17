# OptiAwsS3Upload
The optis3upload Android library is AWS' preferred mechanism for interacting
with AWS services from an Android device using Amplify.

The library provides a high-level interface to perform different
**categories** of cloud operations. Each category may be fulfilled by a
**plugin**, which you configure during setup.

## Platform Support

The optis3upload Framework supports Android API level 16 (Android 4.1) and above.

## Using optis3upload from Your App
### Specifying Gradle Dependencies

To begin, copy optis3upload module to your `app` and add in `build.gradle`
dependencies section:
```groovy
implementation project(path: ':optis3upload')

settings.gradle
include ':optis3upload'

change s3bucket settings in res/raw/amplifyconfiguration.json and res/raw/awsconfiguration.json
```
### Java 8 Requirement

Amplify Android _requires_ Java 8 features. Please add a `compileOptions`
block inside your app's `build.gradle`, as below:

```gradle
android {
    compileOptions {
        coreLibraryDesugaringEnabled true
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```
##Upload File
```gradle
-Upload via InputStream
S3FileUpload(S3UploadListener.class).uploadInputStream(reference_id,this,Uri,keyname)

-Upload via File
S3FileUpload(S3UploadListener.class).uploadFile(reference_id,Activity,File,keyname)

for Coroutines change method name to uploadInputStreamCoroutines and uploadFileCoroutines
```

##Download File
```gradle
-Download File
 S3FileDownload(S3DownloadListener.class).downloadFile(reference_id,File,keyname)

-Generate Download URL
 S3FileDownload(S3DownloadListener.class).generateURL(reference_id,keyname)

for Coroutines change method name to downloadFileCoroutines and generateURLCoroutines
```
##Remove File
```gradle
-Remove File
S3FileDelete(S3RemoveListener.class).deleteFile(reference_id,keyname)

for Coroutines change method name to deleteFileCoroutines
```
##Files List
```gradle
-Files List
S3FileList(S3FileListListener.class).getFiles(path)

for Coroutines change method name to getFilesCoroutines
```


## License

This library is licensed under the [Apache 2.0 License](./LICENSE).

## Report a Bug

We appreciate your feedback -- comments, questions, and bug reports. Please
[submit a GitHub issue](https://github.com/jaiobs/OptiAwsS3Upload/issues),
and we'll get back to you.