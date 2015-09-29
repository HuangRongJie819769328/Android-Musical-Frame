package com.hrj.mymusickuangjia.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.hrj.mymusickuangjia.configure.MyVariable;

public class MySerice extends Service {
	/**
	 * 将对象给service，保证不被内存回收，运行于后台
	 */
	MediaPlayer mediaPlayer = MyVariable.getMediaPlayer();

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
