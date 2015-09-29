package com.hrj.mymusickuangjia.myinterface;

import android.media.MediaPlayer;

/**
 * 自定义 音乐资源准备的时候 
 * 可能某资源有问题，异步准备了两次，系统api问题，mediaPlayer.prepareAsync();
 * 
 * @author 黄荣洁
 */
public interface MyMusicPrepare {

	public void onStart(MediaPlayer arg0);

	public void onEnd(MediaPlayer arg0);
}
