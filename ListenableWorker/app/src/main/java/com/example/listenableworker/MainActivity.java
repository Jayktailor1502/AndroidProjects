package com.example.listenableworker;

import static androidx.work.WorkManager.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.multiprocess.RemoteWorkerService;

import android.content.ComponentName;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private String PACKAGE_NAME = "com.example.listenableworker" ;
    WorkManager workManager = null;
    Button btn, btn1 ;
    TextView tx , tx1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workManager = WorkManager.getInstance(this);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        btn1 = findViewById(R.id.btn1);
        tx = findViewById(R.id.textw);
        tx1 = findViewById(R.id.textlw);
    }

    public void Worker(View view) {
        OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(SimpleWorker.class).build();
        workManager.enqueue(mRequest);
        workManager.getWorkInfoByIdLiveData(mRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                String status = workInfo.getState().name();
                tx.setText(status);
            }
        });
    }

    public void ListenableWorker(View view) {
        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(ListenableWorkerExample.class)
                .setConstraints(constraints)
                .build();

        workManager.enqueue(mRequest);

        workManager.getWorkInfoByIdLiveData(mRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                String status = workInfo.getState().name();
                tx1.setText(status);
            }
        });
    }
}