package com.hrj.mymusickuangjia;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;

import com.hrj.mymusickuangjia.configure.MyConstant;
import com.hrj.mymusickuangjia.configure.MyVariable;
import com.hrj.mymusickuangjia.myinterface.MyMusicEnd;
import com.hrj.mymusickuangjia.myinterface.MyMusicListListen;
import com.hrj.mymusickuangjia.myinterface.MyMusicPrepare;
import com.hrj.mymusickuangjia.service.MySerice;

/**
 * 音乐播放器框架调用方法
 * 
 * @author 黄荣洁
 * 
 */
public class MyMediaPlayer {

	static MediaPlayer mediaPlayer;

	/**
	 * handler运行在主线程
	 */
	static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 进度线程
			case MyConstant.handler_what_musicprogress:
				MyVariable.getMyMusicListListen().onProgress(
						mediaPlayer.getCurrentPosition(),
						mediaPlayer.getDuration());
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 初始化资源
	 */
	public static void init(final Context context) {
		mediaPlayer = new MediaPlayer();
		MyVariable.setContext(context);

		MyVariable.setMediaPlayer(mediaPlayer);

		context.startService(new Intent(context, MySerice.class));
	};

	/**
	 * 开始音乐
	 * 
	 * @param path
	 *            播放资源路径 mp.setDataSource("/sdcard/test.mp3");
	 *            mp.setDataSource("http://www.citynorth.cn/music/confucius.mp3"
	 *            );
	 */
	public static void start(String path) {
		try {
			// mediaPlayer = new MediaPlayer();
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			// 资源准备
			mediaPlayer.prepareAsync();
			// 判断用户有没有设置自己的资源准备事件
			MyMusicPrepare myMusicPrepare = MyVariable.getMyMusicPrepare();
			if (null == myMusicPrepare)// 没有
				mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
					public void onPrepared(MediaPlayer arg0) {
						mediaPlayer.start();
					}
				});
			else {// 有
					// 警告，此处有bug，详情请看XXX
				if (!MyVariable.getFlag_bug_myMusicPrepare()) {
					myMusicPrepare.onStart(mediaPlayer);
					MyVariable.setFlag_bug_myMusicPrepare(true);
				}
				setOnPreparedListener(MyVariable.getMyMusicPrepare());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开始音乐
	 * 
	 * @param R_raw
	 *            通过raw资源播放
	 */
	public static void start(int R_raw) {
		MyVariable.setMediaPlayer(MediaPlayer.create(MyVariable.getContext(),
				R_raw));
		mediaPlayer.start();
	}

	/**
	 * 修改播放音乐的模式
	 * 
	 * @param mode
	 */
	public static void changmode(int mode) {
		switch (mode) {
		case MyConstant.MODE_Order:// 顺序播放

			MyVariable.setMme_changmode(new MyMusicEnd() {
				public void onCompletion(MediaPlayer arg0) {
					int index = MyVariable.getIndex();
					ArrayList<String> paths = MyVariable.getPaths();
					MyMusicListListen myMusicListListen = MyVariable
							.getMyMusicListListen();

					if (++index == paths.size())
						index = 0;
					MyVariable.setIndex(index);
					start(paths.get(index));
					myMusicListListen.onPosition(index);
				}
			});

			break;
		case MyConstant.MODE_Random:// 随机播放

			MyVariable.setMme_changmode(new MyMusicEnd() {
				public void onCompletion(MediaPlayer arg0) {
					ArrayList<String> paths = MyVariable.getPaths();
					MyMusicListListen myMusicListListen = MyVariable
							.getMyMusicListListen();

					MyVariable.setIndex((int) (Math.random() * paths.size()));
					int index = MyVariable.getIndex();
					start(paths.get(index));
					myMusicListListen.onPosition(index);
				}
			});

			break;
		case MyConstant.MODE_Cycle:// 单曲循环

			MyVariable.setMme_changmode(new MyMusicEnd() {
				public void onCompletion(MediaPlayer arg0) {
					int index = MyVariable.getIndex();
					ArrayList<String> paths = MyVariable.getPaths();
					MyMusicListListen myMusicListListen = MyVariable
							.getMyMusicListListen();

					start(paths.get(index));
					myMusicListListen.onPosition(index);
				}
			});

			break;
		default:
			break;

		}

		// 设置播放模式
		setOnCompletionListener(MyVariable.getMme_changmode());
	}

	/**
	 * 对一堆音乐进行操作，随机播放，顺序播放，单曲循环
	 * 
	 * @param paths
	 *            要播放的所有音乐的路径集合
	 * @param position
	 *            要播放的音乐在集合中的位置
	 * @param mode
	 *            MODE_Order 顺序播放 MODE_Random 随机播放 MODE_Cycle 单曲循环
	 * @param myMusicListListen
	 *            对音乐播放列表的监听事件
	 */
	public static void start(final ArrayList<String> paths, int position,
			int mode, final MyMusicListListen myMusicListListen) {
		// 设置数据
		MyVariable.setIndex(position);
		MyVariable.setPaths(paths);
		MyVariable.setMyMusicListListen(myMusicListListen);
		MyVariable.setFlag_thread_alive(false);// 停止之前的线程

		start(paths.get(position));
		// 正在播放的音乐在列表的位置
		myMusicListListen.onPosition(position);
		// 正在播放音乐进度的监听事件
		new Thread() {
			public void run() {
				try {// 保证关闭之前开启的线程
					sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				MyVariable.setFlag_thread_alive(true);
				while (MyVariable.getFlag_thread_alive()) {
					try {
						handler.sendEmptyMessage(MyConstant.handler_what_musicprogress);
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
		// 修改mode
		changmode(mode);

	}

	/**
	 * 继续音乐
	 */
	public static void continuation() {
		if (null != mediaPlayer)
			mediaPlayer.start();
	}

	/**
	 * 暂停音乐
	 */
	public static void pause() {
		if (null != mediaPlayer && mediaPlayer.isPlaying())
			mediaPlayer.pause();
	}

	/**
	 * 结束音乐
	 */
	public static void stop() {
		if (null != mediaPlayer && mediaPlayer.isPlaying())
			mediaPlayer.stop();
	}

	/**
	 * 正在播放？
	 */
	public static Boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	/**
	 * 将音乐移到指定位置播放
	 * 
	 * @param index
	 */
	public static void seekTo(int index) {
		if (mediaPlayer.isPlaying())
			mediaPlayer.pause();
		mediaPlayer.seekTo(index);
	}

	/**
	 * 上一首歌
	 * 
	 * @return false表示切换失败
	 */
	public static Boolean lastMusic() {
		int index = MyVariable.getIndex();
		if (index == 0)
			return false;
		if (mediaPlayer.isPlaying())
			mediaPlayer.stop();
		MyVariable.setIndex(--index);
		start(MyVariable.getPaths().get(index));
		MyVariable.getMyMusicListListen().onPosition(index);
		return true;
	}

	/**
	 * 下一首歌
	 * 
	 * @return false表示切换失败
	 */
	public static Boolean nextMusic() {
		int index = MyVariable.getIndex();
		if (index == MyVariable.getPaths().size() - 1)
			return false;
		if (mediaPlayer.isPlaying())
			mediaPlayer.stop();
		MyVariable.setIndex(++index);
		start(MyVariable.getPaths().get(index));
		MyVariable.getMyMusicListListen().onPosition(index);
		return true;
	}

	/**
	 * 音乐完成播放回调事件
	 * 
	 * @param mme
	 */
	public static void setOnCompletionListener(final MyMusicEnd mme) {
		if (null == mme)
			return;
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer arg0) {
				// 释放资源
				mediaPlayer.stop();
				mme.onCompletion(arg0);
			}
		});
	}

	/**
	 * 资源准备监听事件
	 * 
	 * @param myMusicPrepare
	 */
	public static void setOnPreparedListener(final MyMusicPrepare myMusicPrepare) {
		MyVariable.setMyMusicPrepare(myMusicPrepare);
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			public void onPrepared(MediaPlayer arg0) {
				mediaPlayer.start();
				myMusicPrepare.onEnd(arg0);
				// bug，解锁，允许下次调用myMusicPrepare的onstart()
				MyVariable.setFlag_bug_myMusicPrepare(false);
			}
		});
	}

}
