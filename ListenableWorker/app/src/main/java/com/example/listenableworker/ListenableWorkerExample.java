package com.example.listenableworker;

import static android.os.AsyncTask.*;

import android.content.Context;
import android.os.AsyncTask;
import android.telecom.Call;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.concurrent.futures.ResolvableFuture;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.android.volley.Response;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Future;

import javax.security.auth.callback.Callback;

import kotlinx.coroutines.CoroutineScope;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListenableWorkerExample extends ListenableWorker {

    public final String TAG = "ListenableWorker";
    private ResolvableFuture<Result> mFuture = ResolvableFuture.create();

    public ListenableWorkerExample(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public ListenableFuture<ListenableWorker.Result> startWork() {
        Log.e(TAG, "Started... ");
        return CallbackToFutureAdapter.getFuture(completer -> {
            Log.e(TAG, "Started... (in)");
            try{
                Thread.sleep(5000);
                Log.e(TAG, "Started...1 ");
                for(int i = 0;i<10;i++)
                {
                    Thread.sleep(1000);
                    Log.e("LW","i ="+i);
                }
                Log.e(TAG,"Completed");
                return completer.set(Result.success());
            } catch (Exception e){
                e.printStackTrace();
                return completer.set(Result.failure());
            }
        });
    }

    public class doAsyncTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            String current = "";
            try {
//                URL url;
//                HttpURLConnection urlConnection = null;
//                try {
////                    url = new URL("https://www1.mobileappdatabase.in/?tm=1&subid4=1636370643.0172290000&kw=APK&KW1=Download%20APK%20from%20Google%20Enterprise%20Cloud%20Storage&KW2=Mobile%20Device%20Management&KW3=Android%20Application%20Development%20Software&KW4=Network%20Emulator%20Software&KW5=Android%20Web%20Developer%20APK&searchbox=0&domainname=0&backfill=0");
////                    urlConnection = (HttpURLConnection) url.openConnection();
////                    InputStream in = urlConnection.getInputStream();
////                    InputStreamReader isw = new InputStreamReader(in);
////
////                    int data = isw.read();
////                    while (data != -1) {
////                        current += (char) data;
////                        data = isw.read();
////                        System.out.print(current);
////                    }
////                    Log.d("LW","Completed.");
//                    Thread.sleep(5000);
//                    Log.d("LW","Completed.");
//                    // return the data to onPostExecute method
//                    return current;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (urlConnection != null) {
//                        urlConnection.disconnect();
//                    }
                Thread.sleep(5000);
                Log.e(TAG, "Started...1 ");
                for(int i = 0;i<10;i++)
                {
                    Thread.sleep(1000);
                    Log.e("LW","i ="+i);
                }
                Log.e(TAG,"Completed");
                }

             catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }
    }

}