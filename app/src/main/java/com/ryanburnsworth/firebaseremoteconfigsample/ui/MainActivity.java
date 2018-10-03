package com.ryanburnsworth.firebaseremoteconfigsample.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ryanburnsworth.firebaseremoteconfigsample.R;
import com.ryanburnsworth.firebaseremoteconfigsample.model.RemoteConfig;
import com.ryanburnsworth.firebaseremoteconfigsample.model.RemoteConfigClient;

public class MainActivity extends AppCompatActivity {
    private LiveData<RemoteConfig> remoteConfig;
    private LinearLayout container;
    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (LinearLayout) findViewById(R.id.container);
        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);

        RemoteConfigClient remoteConfigClient = new RemoteConfigClient();
        remoteConfig = remoteConfigClient.getRemoteConfig();

        observeRemoteConfig();
    }

    private void observeRemoteConfig() {
        remoteConfig.observe(this, new Observer<RemoteConfig>() {
            @Override
            public void onChanged(@Nullable RemoteConfig remoteConfig) {
                if (remoteConfig != null) {
                    container.setBackgroundColor(Color.parseColor(remoteConfig.getBgcolor()));
                    textView.setText(remoteConfig.getMessage());

                    if (remoteConfig.isDisplayImage())
                        imageView.setVisibility(View.VISIBLE);
                    else
                        imageView.setVisibility(View.GONE);
                }
            }
        });
    }
}