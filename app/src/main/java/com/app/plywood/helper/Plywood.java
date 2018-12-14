package com.app.plywood.helper;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import java.io.File;

@ReportsCrashes(mailTo = "droidcodermyself@gmail.com")
public class Plywood extends Application {

    public static Plywood mInstance;
    public static File file;

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
