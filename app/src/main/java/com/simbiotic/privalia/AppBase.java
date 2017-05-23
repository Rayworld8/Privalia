package com.simbiotic.privalia;

import android.app.Application;
import android.content.Context;

import com.simbiotic.privalia.domain.events.BusEvent;
import com.simbiotic.privalia.domain.usecases.UseCase;


import de.greenrobot.event.EventBus;



public class AppBase extends Application {

    private static AppBase sInstance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        context = getApplicationContext();
        EventBus.getDefault().register(this);
    }

    public static synchronized AppBase getInstance() {
        return sInstance;
    }

    public static String getApplicationId() {
        return BuildConfig.APPLICATION_ID;
    }

    public void onEventAsync(UseCase usecase) {
        usecase.execute();
    }

    public void onEvent(BusEvent event) {
        EventBus.getDefault().cancelEventDelivery(event);
    }



}
