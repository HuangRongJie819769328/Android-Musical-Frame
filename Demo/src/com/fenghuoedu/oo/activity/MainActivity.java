package com.fenghuoedu.oo.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.fenghuoedu.oo.model.MyAudio;
import com.fenghuoedu.oo.tool.ReadDataFromContentProvider;
import com.hrj.mymusickuangjia.MyMediaPlayer;
import com.hrj.mymusickuangjia.configure.MyConstant;
import com.hrj.mymusickuangjia.myinterface.MyMusicListListen;
import com.hrj.mymusickuangjia.myinterface.MyMusicPrepare;

public class MainActivity extends Activity {

	@ViewInject(id = R.id.main_bt_start, click = "start")
	Button main_bt_start;
	@ViewInject(id = R.id.main_bt_pause, click = "pause")
	Button main_bt_pause;
	@ViewInject(id = R.id.main_bt_stop, click = "stop")
	Button main_bt_stop;
	@ViewInject(id = R.id.main_bt_last, click = "last")
	Button main_bt_last;
	@ViewInject(id = R.id.main_bt_next, click = "next")
	Button main_bt_next;

	@ViewInject(id = R.id.listview)
	private ListView listview;
	private List<MyAudio> data;

	private int[] datasize;// 播放的音乐，标题变成緑色，1是緑，0是黑
	final int Datasize_none = -1;// 没有歌曲在播放
	int datawz = 0;// 记录 播放列表的那一首歌曲
	int mode = MyConstant.MODE_Order;// 播放音乐模式，默认顺序播放
	ArrayList<String> paths;// 存放本地音乐路径

	private BaseAdapter adapter;
	@ViewInject(id = R.id.root)
	private LinearLayout root;

	@ViewInject(id = R.id.main_tv_mode)
	TextView main_tv_mode;

	@ViewInject(id = R.id.main_sb)
	SeekBar main_sb;

	AlertDialog ad_progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FinalActivity.initInjectedView(this);// 启用final框架，侵入activity

		initview();// 初始化开始暂停停止
		initdate();// 加载数据

	}

	/**
	 * 初始化开始暂停停止
	 */
	private void initview() {
		main_bt_start.setEnabled(true);
		main_bt_pause.setEnabled(false);
		main_bt_pause.setText("暂停");
		main_bt_stop.setEnabled(false);

	}

	/**
	 * 开始
	 * 
	 * @param v
	 */
	public void start(View v) {

		// MyMediaPlayer.start(data.get(datawz).getPath());
		// 启动音乐
		startmusic(datawz);
		main_bt_start.setEnabled(false);
		main_bt_pause.setEnabled(true);
		main_bt_stop.setEnabled(true);

	}

	/**
	 * 暂停继续
	 * 
	 * @param v
	 */
	public void pause(View v) {
		if ("继续".equals(main_bt_pause.getText().toString())) {
			MyMediaPlayer.continuation();// 表示要暂停还是继续 true时表示暂停
			main_bt_pause.setText("暂停");
		} else {
			MyMediaPlayer.pause();
			main_bt_pause.setText("继续");
		}
	}

	/**
	 * 停止
	 * 
	 * @param v
	 */
	public void stop(View v) {
		MyMediaPlayer.stop();
		main_bt_start.setEnabled(true);
		main_bt_pause.setEnabled(false);
		main_bt_pause.setText("暂停");
		main_bt_stop.setEnabled(false);
	}

	/**
	 * 上一首
	 * 
	 * @param v
	 */
	public void last(View v) {
		if (!MyMediaPlayer.lastMusic())
			Toast.makeText(this, "上一首播放失败", 0).show();
	}

	/**
	 * 下一首
	 * 
	 * @param v
	 */
	public void next(View v) {
		if (!MyMediaPlayer.nextMusic())
			Toast.makeText(this, "下一首播放失败", 0).show();
	}

	/**
	 * 加载数据 不要放在onCreate里
	 */
	private void initdate() {

		data = ReadDataFromContentProvider.readAudio(this);
		if (null == data || data.size() == 0) {
			Toast.makeText(this, "没有扫描到音频文件!", Toast.LENGTH_LONG).show();
			return;
		}
		datasize = new int[data.size()];

		adapter = new AudioAdapter(this, data, datasize);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (MyMediaPlayer.isPlaying())
					MyMediaPlayer.stop();
				// 更新开始暂停
				start(main_bt_start);
				// 开始音乐
				startmusic(arg2);
			}
		});
		// 获取所有本地音乐路径
		paths = new ArrayList<String>();
		for (int i = 0; i < data.size(); i++) {
			paths.add(data.get(i).getPath());
		}
		// seekbar拖动事件
		main_sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar arg0) {
				// 继续播放
				MyMediaPlayer.continuation();
			}

			public void onStartTrackingTouch(SeekBar arg0) {
				// 暂停播放
				if (MyMediaPlayer.isPlaying())
					MyMediaPlayer.pause();
			}

			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// 如果是用户触发的,移动到指定位置
				if (arg2)
					MyMediaPlayer.seekTo(arg1);
			}
		});
		// 设置音乐播放器资源准备事件
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
	}

	/**
	 * datasize位置初始化,即那首歌正在播放
	 * 
	 * @param wz
	 *            那首歌正在播放的位置
	 */
	private void datasizeinit(int wz) {
		// 重置颜色
		for (int i = 0; i < datasize.length; i++)
			datasize[i] = 0;
		// 设置颜色
		if (wz != Datasize_none) {
			datasize[wz] = 1;
			datawz = wz;
		}
		// 刷新界面
		adapter.notifyDataSetChanged();
		listview.setSelection(wz);
	}

	/**
	 * 有两个地方需要启动音乐，抽取出来成为方法 启动music
	 * 
	 * @param wz
	 */
	private void startmusic(int wz) {
		MyMediaPlayer.start(paths, wz, mode, new MyMusicListListen() {
			public void onProgress(long arg0, long arg1) {
				// 更新音乐播放进度
				main_sb.setProgress((int) arg0);
				main_sb.setMax((int) arg1);
			}

			public void onPosition(int arg0) {
				// 更新界面
				datasizeinit(arg0);

			}
		});
	}

	/**
	 * 单曲循环
	 * 
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
	 * 
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
	 * 
	 * @param v
	 */
	public void bt_click_sxbf(View v) {
		mode = MyConstant.MODE_Order;
		MyMediaPlayer.changmode(mode);
		main_tv_mode.setText("顺序播放模式");
		Toast.makeText(this, "顺序播放模式启动", 0).show();
	}

	class AudioAdapter extends BaseAdapter {
		private Context context;
		private List<MyAudio> data;
		private int[] datasize;

		public AudioAdapter(Context context, List<MyAudio> data, int[] datasize) {
			super();
			this.context = context;
			this.data = data;
			this.datasize = datasize;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (null != convertView) {
				view = convertView;
			} else {
				view = LayoutInflater.from(context).inflate(
						android.R.layout.simple_list_item_2, null);
			}

			TextView tv1 = (TextView) view.findViewById(android.R.id.text1);
			tv1.setText(data.get(position).getTitle());
			if (datasize[position] == 1)// 标题是黑是白
				tv1.setTextColor(Color.GREEN);
			else
				tv1.setTextColor(Color.BLACK);

			TextView tv2 = (TextView) view.findViewById(android.R.id.text2);
			tv2.setText(data.get(position).getArtist() + "\t"
					+ data.get(position).getAlbum());
			return view;
		}

	}
}
