package com.app.plywood.helper;

import android.app.Application;
import android.content.Context;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(mailTo = "droidcodermyself@gmail.com")
public class Plywood extends Application {

    public static Plywood mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
        mInstance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
    }
}
