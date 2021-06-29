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
## Upload File
```gradle
s3UploadSession = OptiAWSFactory.createUploadAwsSession(this)
-Upload via InputStream
s3UploadSession.uploadInputStream(reference_id,this,Uri,keyname)

-Upload via File
s3UploadSession.uploadFile(reference_id,Activity,File,keyname)

for Coroutines change method name to uploadInputStreamCoroutines and uploadFileCoroutines
```

## Download File
```gradle
s3DownloadSession = OptiAWSFactory.createDownloadAwsSession(this)
-Download File
 s3DownloadSession.downloadFile(reference_id,File,keyname)

-Generate Download URL
 s3DownloadSession.generateURL(reference_id,keyname)

for Coroutines change method name to downloadFileCoroutines and generateURLCoroutines
```
## Remove File
```gradle
s3RemoveSession = OptiAWSFactory.createRemoveAwsSession(this)
-Remove File
s3RemoveSession.deleteFile(reference_id,keyname)

for Coroutines change method name to deleteFileCoroutines
```
## Files List
```gradle
s3FileListSession = OptiAWSFactory.createFilesListAwsSession(this)
-Files List
s3FileListSession.getFiles(path)

for Coroutines change method name to getFilesCoroutines
```
## Configuration File
under raw folder create amplifyconfiguration.json and awsconfiguration.json
update your S3 configuration

##amplifyconfiguration.json
{
  "auth": {
    "plugins": {
      "awsCognitoAuthPlugin": {
        "IdentityManager": {
          "Default": {}
        },
        "CredentialsProvider": {
          "CognitoIdentity": {
            "Default": {
              "PoolId": "xx-xxxx-x:xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
              "Region": "xx-xxxx-x"
            }
          }
        },
        "CognitoUserPool": {
          "Default": {
            "PoolId": "xx-xxxx-x_xxxxxxxxx",
            "AppClientId": "xxxxxxxxxxxxxx",
            "Region": "xx-xxxx-x"
          }
        },
        "Auth": {
          "Default": {
            "authenticationFlowType": "USER_SRP_AUTH"
          }
        }
      }
    }
  },
  "storage": {
    "plugins": {
      "awsS3StoragePlugin": {
        "bucket": "xxxxxx",
        "region": "xx-xxxx-x"
      }
    }
  }
}

##awsconfiguration
{
  "Version": "1.0",
  "IdentityManager": {
    "Default": {}
  },
  "CredentialsProvider": {
    "CognitoIdentity": {
      "Default": {
        "PoolId": "xx-xxxx-x:xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
        "Region": "xx-xxxx-xx"
      }
    }
  },
  "S3TransferUtility": {
    "Default": {
      "Bucket": "xxx-xxx",
      "Region": "xx-xxxx-x"
    }
  },
  "CognitoUserPool": {
    "Default": {
      "PoolId": "xx-xxxx_xxxxxxx",
      "AppClientId": "xxxxxxxxxxxxxxxxxxxxxxxxx",
      "AppClientSecret": "xxxxxxxxxxxxx",
      "Region": "xx-xxxx-x"
    }
  }
}

## License

This library is licensed under the [Apache 2.0 License](./LICENSE).

## Report a Bug

We appreciate your feedback -- comments, questions, and bug reports. Please
[submit a GitHub issue](https://github.com/jaiobs/OptiAwsS3Upload/issues),
and we'll get back to you.