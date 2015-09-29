package com.hrj.mymusickuangjia.configure;

import java.util.ArrayList;

import android.content.Context;
import android.media.MediaPlayer;

import com.hrj.mymusickuangjia.myinterface.MyMusicEnd;
import com.hrj.mymusickuangjia.myinterface.MyMusicListListen;
import com.hrj.mymusickuangjia.myinterface.MyMusicPrepare;

/**
 * 存放变量
 * 
 * @author 黄荣洁
 * 
 */
public class MyVariable {
	/**
	 * MediaPlayer
	 */
	private static MediaPlayer mediaPlayer;
	/**
	 * 上下文
	 */
	private static Context context;

	// 音乐播放模式需要用到的变量--------------------------------------
	// --------------------------------------
	/**
	 * 音乐播放模式关键接口,每次修改mode都要使用
	 */
	private static MyMusicEnd mme_changmode;
	/**
	 * 要播放的音乐在集合中的位置
	 */
	private static int index;
	/**
	 * 要播放的所有音乐的路径集合
	 */
	private static ArrayList<String> paths;
	/**
	 * 对音乐播放列表的监听事件
	 */
	private static MyMusicListListen myMusicListListen;
	/**
	 * 需要开启一个线程监听音乐播放器的进度
	 */
	private static Boolean flag_thread_alive;
	/**
	 * 音乐资源准备的监听事件
	 */
	private static MyMusicPrepare myMusicPrepare;

	// --------------------------------------
	// 音乐播放模式需要用到的变量--------------------------------------

	// 为了解决bug的变量--------------------------------------
	// --------------------------------------
	/**
	 * 该变量用于解决MyMusicPrepare接口重复调用onstart方法的判断
	 */
	private static Boolean flag_bug_myMusicPrepare = false;

	// --------------------------------------
	// 为了解决bug的变量--------------------------------------

	public static MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public static void setMediaPlayer(MediaPlayer mediaPlayer) {
		MyVariable.mediaPlayer = mediaPlayer;
	}

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context context) {
		MyVariable.context = context;
	}

	public static MyMusicEnd getMme_changmode() {
		return mme_changmode;
	}

	public static void setMme_changmode(MyMusicEnd mme_changmode) {
		MyVariable.mme_changmode = mme_changmode;
	}

	public static int getIndex() {
		return index;
	}

	public static void setIndex(int index) {
		MyVariable.index = index;
	}

	public static ArrayList<String> getPaths() {
		return paths;
	}

	public static void setPaths(ArrayList<String> paths) {
		MyVariable.paths = paths;
	}

	public static MyMusicListListen getMyMusicListListen() {
		return myMusicListListen;
	}

	public static void setMyMusicListListen(MyMusicListListen myMusicListListen) {
		MyVariable.myMusicListListen = myMusicListListen;
	}

	public static Boolean getFlag_thread_alive() {
		return flag_thread_alive;
	}

	public static void setFlag_thread_alive(Boolean flag_thread_alive) {
		MyVariable.flag_thread_alive = flag_thread_alive;
	}

	public static MyMusicPrepare getMyMusicPrepare() {
		return myMusicPrepare;
	}

	public static void setMyMusicPrepare(MyMusicPrepare myMusicPrepare) {
		MyVariable.myMusicPrepare = myMusicPrepare;
	}

	public static Boolean getFlag_bug_myMusicPrepare() {
		return flag_bug_myMusicPrepare;
	}

	public static void setFlag_bug_myMusicPrepare(
			Boolean flag_bug_myMusicPrepare) {
		MyVariable.flag_bug_myMusicPrepare = flag_bug_myMusicPrepare;
	}

}
