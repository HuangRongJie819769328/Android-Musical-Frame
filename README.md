# Android-Musical-Frame
Introduction:
	This music code framework is the API Android again package, easy music developers call! Features include: play, pause, stop, stop, on one, the next one, the order of play, random play, single cycle
	这音乐代码框架是对android api的再次封装，方便音乐开发者的调用！功能包括：播放，暂停，继续，停止，上一首，下一首，顺序播放，随机播放，单曲循环

Using method:

1.Configure

	add manifest

        <service android:name="com.hrj.mymusickuangjia.service" />

2.Initialization

	public class MyApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		// you can initialization
		MyMediaPlayer.init(this);


	}

3.Method

	MyMediaPlayer.start(data.get(datawz).getPath());

	MyMediaPlayer.start(int R_raw);

	/**
	 *The operation of a pile of music, random play, the order of play, single cycle
	 * 对一堆音乐进行操作，随机播放，顺序播放，单曲循环
	 * 
	 * @param paths
	 *	      The path set for all music to be played
	 *            要播放的所有音乐的路径集合
	 * @param position
	 *	      To play the music in the collection
	 *            要播放的音乐在集合中的位置
	 * @param mode
	 *	      MODE_Order sequence of MODE_Random random play MODE_Cycle cycle
	 *            MODE_Order 顺序播放 MODE_Random 随机播放 MODE_Cycle 单曲循环
	 * @param myMusicListListen
	 *	      Monitor event for music player
	 *            对音乐播放列表的监听事件
	 * ArrayList<String> paths, int position,int mode, MyMusicListListen myMusicListListen
	 */
	MyMediaPlayer.start(paths, wz, mode, new MyMusicListListen() {
			public void onProgress(long arg0, long arg1) {
				// 更新音乐播放进度
				//Update music playback
				main_sb.setProgress((int) arg0);
				main_sb.setMax((int) arg1);
			}

			public void onPosition(int arg0) {
				// 更新界面
				/ / the current playing position
				datasizeinit(arg0);

			}
		});

	MyMediaPlayer.continuation();

	MyMediaPlayer.pause();

	MyMediaPlayer.stop();

	MyMediaPlayer.lastMusic();//is return boolean

	MyMediaPlayer.nextMusic();//is return boolean

	MyMediaPlayer.setOnPreparedListener(new MyMusicPrepare() {
		public void onStart(MediaPlayer arg0) {
			ad_progress = new AlertDialog.Builder(MainActivity.this)
					.setView(new ProgressBar(MainActivity.this))
					.setCancelable(false).show();

		}

		public void onEnd(MediaPlayer arg0) {
			ad_progress.cancel();

		}
	});

4.

	/**
	 * 单曲循环
	 * Cycle
	 * @param v
	 */
	public void bt_click_dqxh(View v) {
		mode = MyConstant.MODE_Cycle;
		MyMediaPlayer.changmode(mode);
		main_tv_mode.setText("单曲循环模式");
		Toast.makeText(this, "单曲循环模式启动", 0).show();
	}

	/**
	 * 随机播放
	 * Random
	 * @param v
	 */
	public void bt_click_sjbf(View v) {
		mode = MyConstant.MODE_Random;
		MyMediaPlayer.changmode(mode);
		main_tv_mode.setText("随机播放模式");
		Toast.makeText(this, "随机播放模式启动", 0).show();
	}

	/**
	 * 顺序播放
	 * Order
	 * @param v
	 */
	public void bt_click_sxbf(View v) {
		mode = MyConstant.MODE_Order;
		MyMediaPlayer.changmode(mode);
		main_tv_mode.setText("顺序播放模式");
		Toast.makeText(this, "顺序播放模式启动", 0).show();
	}
}