package com.fenghuoedu.oo.activity;

import android.app.Application;

import com.hrj.mymusickuangjia.MyMediaPlayer;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		MyMediaPlayer.init(this);

	}
}
