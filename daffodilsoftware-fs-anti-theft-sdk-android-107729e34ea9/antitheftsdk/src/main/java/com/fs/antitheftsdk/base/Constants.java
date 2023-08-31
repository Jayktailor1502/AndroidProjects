package com.fs.antitheftsdk.base;

public class Constants {


    public static final String TAG="AntiTheft Logs";
    public static final String ACTION = "Action";
    public static final String ALARM = "AlarmOn";
    public static final String ALARM_OFF = "AlarmOff";
    public static final String LOCATION = "Location";
    public static final String CAMERA = "Camera";
    public static final String MODE = "Mode";
    public static final Long BATTERY_INTERVAL = (long) (30 * 1000);
    public static final Integer BATTERY_LEVEL_10 = 10;
    public static final Integer BATTERY_LEVEL_5 = 5;
    public static final Integer BATTERY_LEVEL_1 = 1;
    public static final String REMOTE_LOCKING = "RemoteLocking";
    public static final String REMOTE_WIPE = "RemoteWipe";
    public static final String isAlive = "isAlive";
    public static final String BASE_URL = "Base_Url";
    public static final String VERSION_NAME = "Version_Name";
    public static final Object REMOTE_UNLOCKING = "RemoteUnlocking";
    public static final String IMAGE_TYPE_PNG = "png";
    public static final String BROADCAST_LOGIN = "BROADCAST_LOGIN";
    public static final String BROADCAST_CAMERA = "BROADCAST_CAMERA";
    public static final String BROADCAST_ALARM = "BROADCAST_ALARM";
    public static final String REQUEST_EXTRA_LOGIN = "REQUEST_EXTRA_LOGIN";
    public static int count = 0;
    public static final String BROADCAST_API = "BROADCAST_API";
    public static int onRecieveCounter = 0;
    public static boolean isLock = false;
    public static final String USER_ID = "userId";
    public static final String IMAGE_TYPE = "imageType";
    public static final String UPLOAD_URL = "UPLOADURL";
    public static final String IMAGE_STRING = "IMAGESTRING";
    public static final String CAMERA_MANAGER = "Camera Manager";
    public static final String EXCEPTION = "Exception ";
    public static final String ERROR = "Error";
    public static final String EMAIL="email";
    public static final String MOBILE="mobile";
    public static final String AUTH_TOKEN = "authToken";
    public static final String CONNECT_TIMEOUT = "CONNECT_TIMEOUT";
    public static final String READ_TIMEOUT = "READ_TIMEOUT";
    public static final String WRITE_TIMEOUT = "WRITE_TIMEOUT";
    public static final String APPLICATION_ID = "1VXRFMSa6cqUNrBbEdKktQ";
    public static final String PERMISSION_TYPE="PermissionType";
    public static final String OVERLAY="Overlay";
    public static final String ADMIN="Admin";
    public static final String DISPLAY_OVERLAY="DisplayOverlay";
    public static final String ERROR_OCCURED="Some error occurred. Please try again.";
    public static final String ALREADY_EXISTS="ALREADY_EXISTS";
    public static final String FCM_ERROR="Failed to retreive token";
   public static boolean TriggerStatus=false;

    private Constants() {
    }
}