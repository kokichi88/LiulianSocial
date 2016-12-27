package com.nfury.liulian;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.liulian.game.sdk.SdkManager;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		SdkManager.defaultSDK().applicationOnCreate(this);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		SdkManager.defaultSDK().applicationAttachBaseContext(this, base);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		SdkManager.defaultSDK().applicationOnConfigurationChanged(newConfig);
	}

	@Override
	public void onTerminate() {
		SdkManager.defaultSDK().applicationOnTerminate();
	}
}
