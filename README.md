# OptiAwsS3Upload
<img src="https://s3.amazonaws.com/aws-mobile-hub-images/aws-amplify-logo.png" alt="AWS Amplify" width="550">
The Amplify Android library is AWS' preferred mechanism for interacting
with AWS services from an Android device.

The library provides a high-level interface to perform different
**categories** of cloud operations. Each category may be fulfilled by a
**plugin**, which you configure during setup.

## Platform Support

The Amplify Framework supports Android API level 16 (Android 4.1) and above.

## Using Amplify from Your App
### Specifying Gradle Dependencies

To begin, copy optis3upload module to your `app` and add in `build.gradle`
dependencies section:
```groovy
implementation project(path: ':optis3upload')

settings.gradle
include ':optis3upload'
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

## License

This library is licensed under the [Apache 2.0 License](./LICENSE).

## Report a Bug

We appreciate your feedback -- comments, questions, and bug reports. Please
[submit a GitHub issue](https://github.com/jaiobs/OptiAwsS3Upload/issues),
and we'll get back to you.