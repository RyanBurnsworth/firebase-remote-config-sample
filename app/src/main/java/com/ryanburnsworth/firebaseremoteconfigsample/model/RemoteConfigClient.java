package com.ryanburnsworth.firebaseremoteconfigsample.model;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.ryanburnsworth.firebaseremoteconfigsample.BuildConfig;
import com.ryanburnsworth.firebaseremoteconfigsample.R;
import com.ryanburnsworth.firebaseremoteconfigsample.util.RemoteConfigKeys;

public class RemoteConfigClient {
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private MutableLiveData<RemoteConfig> mutableLiveData = new MutableLiveData<>();
    private int cacheExpiration = 3600;

    public RemoteConfigClient() {
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings settings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true).build();
        firebaseRemoteConfig.setConfigSettings(settings);
        firebaseRemoteConfig.setDefaults(R.xml.remote_config);
        fetchRemoteConfig();
    }

    private void fetchRemoteConfig() {
        if (BuildConfig.DEBUG)
            cacheExpiration = 0;

        firebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            firebaseRemoteConfig.activateFetched();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

        RemoteConfig remoteConfig = new RemoteConfig();
        remoteConfig.setBgcolor(firebaseRemoteConfig.getString(RemoteConfigKeys.BGCOLOR));
        remoteConfig.setDisplayImage(firebaseRemoteConfig.getBoolean(RemoteConfigKeys.DISPLAY_IMAGE));
        remoteConfig.setMessage(firebaseRemoteConfig.getString(RemoteConfigKeys.MESSAGE));
        mutableLiveData.postValue(remoteConfig);
    }

    public MutableLiveData<RemoteConfig> getRemoteConfig() {
        return mutableLiveData;
    }
}
