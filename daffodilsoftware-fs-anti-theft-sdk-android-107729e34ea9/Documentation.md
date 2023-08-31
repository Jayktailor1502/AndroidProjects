# FS AntiTheft Library

FS Anti Theft is mechanism to Ring Alarm, click Pictures, Remote Lock Device,Remote Unlock Device,
Remote Wipe Data,Detect battery.

## Prerequisites

The following are the prerequisites for updating the library

* Gradle 6.5 and above
* Android gradle plugin 4.1.3
* NDK
* Data Binding

> SDK Version:
> Minimum: 21
> Target: 31

## Getting Started

Your project should be build against Android 5.0 (Api 21) and above.

### Project Level Dependencies

In the main project build.gradle file add the following

    allprojects {
        repositories {
        google()
        jcenter()
        mavenCentral()
        maven { url "https://jitpack.io" }
        maven {
                url "${artifactory_url}"
                credentials {
                username = "${artifactory_username}"
                password = "${artifactory_password}"
            }
        }
    }

### App Level Dependencies

The following dependencies are needed to use the SDK:

**Note**: To access antitheft sdk, do add the following dependency

    android {
        buildFeatures {
            dataBinding true
        }
    }
    
    dependencies { 
        implementation "libaray:<latest_version>"
    }

**Note**: If you wish to add firebase crashlytics, add the firebase dependencies too.

    dependencies 
    { 
         implementation platform('com.google.firebase:firebase-bom:28.3.0')
          implementation 'com.google.firebase:firebase-messaging'
          implementation 'com.google.firebase:firebase-messaging-ktx'
          implementation 'com.google.firebase:firebase-analytics'
          implementation 'com.google.firebase:firebase-installations-ktx:17.0.0'
           implementation 'com.google.firebase:firebase-auth:21.0.3' 
    }

### Android Manifest

Open the AndroidManifest.xml of your application and add these permissions:

    <uses-permission android:name="android.permission.INTERNET"/>
        <receiver android:name=".Admin" android:exported="false" android:permission="android.permission.BIND_DEVICE_ADMIN">
            <meta-data android:name="android.app.device_admin" android:resource="@xml/policies"/>
            <intent-filter>
                <action android:name="android.app.action.DEVICE_ADMIN_ENABLED"/>
            </intent-filter>
        </receiver>

        <service
            android:name="com.fs.antitheftsdk.firebase.MyFirebaseMessagingService"
            android:exported="false" >
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
        </service>
        <service
            android:name="com.fs.antitheftsdk.location.LocationService"
            android:enabled="true"
            android:exported="true"/>
        <service android:name="com.fs.antitheftsdk.camera.GetBackCoreService"/>

## SDK Usage Library Initialization:

You can integrate the SDK in your application. First, import the sdk in it.

Then, Create an instance of FsAntiTheft in your onCreate() method of application class.

     FsAntiTheft.init(context, BASE_URL, "Android") 

Then onclick of the registeration button class the method given below

    FsAntiTheft.getInstance().performLoginToSdk(context, email, mobile, ApplicationId);

Then you can call the:

##### Check Permission and Request Permissions open methods:

Class : PermissionsUtil

1. **checkLocationPermission(Context context)**: This method is use to check the location permission
   in application

2. **checkCameraPermission(Context context)**: This method is use to check the Camera permission in
   application

3. **checkPhoneStatePermission(Context context)**: This method is use to check the Phone State
   permission in application.

4. **checkadminPermission(Context context)**: This method is use to check the Admin permission in
   application.

5. **requestAdminPermission(Context context, IRequestPermissionCallback
   iRequestPermissionCallback)**: This method is use to Request the Admin permission in application

6. **checkSinglePermission(Context context, String permission)**: This method is use to check the
   Specific permission in application.

7. **redirectToSettings(Context context)**: This method is use to redirect to the permission setting
   for the application.

8. **requestLocationPermission(Context context, IRequestPermissionCallback
   iRequestPermissionCallback)**:
   This method is use to request for location permission and also get the callback.
   
9. **requestCameraPermission(Context context, IRequestPermissionCallback
   iRequestPermissionCallback)**:
   This method is use to request camera permission. and also get the status in callback.

10. **requestOverlayPermission(Context context, IRequestPermissionCallback
    iRequestPermissionCallback)**:
    This method is use to request the overlay permission
    



