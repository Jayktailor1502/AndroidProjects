
# FS AntiTheft Library
FS Anti Theft is mechanism to Ring Alarm, click Pictures, Remote Lock Device,Remote Unlock Device, Remote Wipe Data,Detect battery.

## Prerequisites
The following are the prerequisites for updating the library

* Gradle 6.5 and above
* Android gradle plugin 4.1.3
* NDK
* Data Binding

>SDK Version:
    >Minimum: 21
    >Target: 31

## Project Dependencies

Following are the dependencies of the project:

|Dependency|Version|
|----------|-------|
/--- androidx.appcompat:appcompat:1.2.0 ---/
/--- com.google.firebase:firebase-analytics:18.0.3--/
/--- com.google.android.material:material:1.4.0---/
/--- junit:junit:4.13.2---/
/--- androidx.test.ext:junit:1.1.3---/
/--- androidx.test.espresso:espresso-core:3.4.0 ---/
/--- com.squareup.retrofit2:retrofit:2.7.1 ---/
/--- com.squareup.retrofit2:converter-gson:2.7.1 ---/
/--- com.squareup.retrofit2:adapter-rxjava2:2.7.1 ---/
/--- com.squareup.okhttp3:okhttp:4.9.0 ---/
/--- com.squareup.okhttp3:logging-interceptor:4.9.0 ---/
/--- io.reactivex.rxjava2:rxjava:2.2.19 ---/
/--- io.reactivex.rxjava2:rxandroid:2.1.1 ---/
/--- androidx.annotation:annotation:1.2.0 ---/
/--- androidx.core:core-ktx:1.3.2 ---/
/--- androidx.constraintlayout:constraintlayout:2.0.4 ---/
/--- org.jetbrains.kotlin:kotlin-stdlib-jdk8 ---/
/--- org.jetbrains.kotlin:kotlin-stdlib: ---/
/--- org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9 ---/
/--- org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.20 ---/
/--- org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2 ---/
/--- org.jetbrains.kotlin:kotlin-bom ---/

    //firebase
/--- com.google.firebase:firebase-bom:28.3.0')
/--- com.google.firebase:firebase-messaging'
/--- com.google.firebase:firebase-messaging-ktx'
/--- com.google.firebase:firebase-analytics'
/--- com.google.firebase:firebase-installations-ktx:17.0.0'
/--- com.google.firebase:firebase-auth:21.0.3'
/--- androidx.browser:browser:1.3.0'
/--- fr.tvbarthel.blurdialogfragment:lib:2.1.5'
/--- androidx.legacy:legacy-support-v4:1.0.0'
/--- com.google.android.gms:play-services-location:19.0.1'
/--- com.google.gms:google-services:4.3.10"

    //Work Manager
/-- implementation "androidx.work:work-runtime-ktx:2.7.1--/


## Project Description
The Project Structure contains 1 module :
- **FsAntiTheftSdk** : This module is the entry point to use this sdk. The class inside "_src/main/java/com/fs/antitheftsdk/sdkClient/_" i.e FsAntiTheftClient, provides all functions of sdk.

#### Project Test list
This Project is basically a sdk that is use tohit Alarm, send the location to web portal, Lock Device, Unlock Device, click pictures, Wipe Data of the device.

1. Alarm
 
2. Location
  -Coarse Location
  - Fine Location
  - Background Location

3. Wipe Data
  - Device Admin Permission
  - Device Policy Manager

4. Remote lock
  - Device Admin
  - READ_PRIVILEGED_PHONE_STATE

5. Remote Unlock
    - Device Admin
    - READ_PRIVILEGED_PHONE_STATE

6. Camera Trigger :
  - main camera

7. Battery Trigger:
  - Location trigger when battery low
  

8. Wrong Password
  - Camera trigger when wrong password thrice



## SDK Usage: 

You can integrate the SDK in your application by first importing the sdk in your project.

Then you need to pass context of your current activity in the FsAntiTheft class method FsAntiTheft.init() and FsAntiTheft.getInstance().performLoginToSdk() to run the further flow of sdk

For more information about sdk callbacks, refer to the sdk usage in [Documentation.md](/Documentation.md).

#Permissions to be added in project
- Admin Permission
- Overlay Permission
- Receivers in the Manifest  


