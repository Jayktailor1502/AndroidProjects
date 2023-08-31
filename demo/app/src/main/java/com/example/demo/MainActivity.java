package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.healthkitsdk.HealthKitSdk;
import com.example.healthkitsdk.model.ActivityType;
import com.example.healthkitsdk.model.EventType;
import com.example.healthkitsdk.model.GraphType;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    HealthKitSdk healthKitSdk;
    GraphType graphType;
    EventType eventType;
    ActivityType activityType;
    LinearLayout ll;
    int[] rgb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btn);
        ll = (LinearLayout) findViewById(R.id.view);

        healthKitSdk = HealthKitSdk.getInstance(this);
        graphType = GraphType.BARCHART;
        eventType = EventType.DAILY;
        activityType = ActivityType.STEPS;

        rgb = new int[]{255, 0, 255};
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (healthKitSdk == null)
            healthKitSdk = HealthKitSdk.getInstance(this);
        getDataFromSdk();
    }

    private void getDataFromSdk() {
        healthKitSdk.init(graphType, eventType, activityType, new Date(), rgb, () -> {
            View graph = healthKitSdk.getGraph();
            if (graph.getParent() != null) {
                ((ViewGroup) graph.getParent()).removeView(graph);
            }
            ll.removeAllViews();
            ll.addView(graph);
        });
    }
}