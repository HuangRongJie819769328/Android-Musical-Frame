# Android-Musical-Frame
Introduction:
	This music code framework is the API Android again package, easy music developers call! Features include: play, pause, stop, stop, on one, the next one, the order of play, random play, single cycle
	�����ִ������Ƕ�android api���ٴη�װ���������ֿ����ߵĵ��ã����ܰ��������ţ���ͣ��������ֹͣ����һ�ף���һ�ף�˳�򲥷ţ�������ţ�����ѭ��

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
	 * ��һ�����ֽ��в�����������ţ�˳�򲥷ţ�����ѭ��
	 * 
	 * @param paths
	 *	      The path set for all music to be played
	 *            Ҫ���ŵ��������ֵ�·������
	 * @param position
	 *	      To play the music in the collection
	 *            Ҫ���ŵ������ڼ����е�λ��
	 * @param mode
	 *	      MODE_Order sequence of MODE_Random random play MODE_Cycle cycle
	 *            MODE_Order ˳�򲥷� MODE_Random ������� MODE_Cycle ����ѭ��
	 * @param myMusicListListen
	 *	      Monitor event for music player
	 *            �����ֲ����б�ļ����¼�
	 * ArrayList<String> paths, int position,int mode, MyMusicListListen myMusicListListen
	 */
	MyMediaPlayer.start(paths, wz, mode, new MyMusicListListen() {
			public void onProgress(long arg0, long arg1) {
				// �������ֲ��Ž���
				//Update music playback
				main_sb.setProgress((int) arg0);
				main_sb.setMax((int) arg1);
			}

			public void onPosition(int arg0) {
				// ���½���
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
	 * ����ѭ��
	 * Cycle
	 * @param v
	 */
	public void bt_click_dqxh(View v) {
		mode = MyConstant.MODE_Cycle;
		MyMediaPlayer.changmode(mode);
		main_tv_mode.setText("����ѭ��ģʽ");
		Toast.makeText(this, "����ѭ��ģʽ����", 0).show();
	}

	/**
	 * �������
	 * Random
	 * @param v
	 */
	public void bt_click_sjbf(View v) {
		mode = MyConstant.MODE_Random;
		MyMediaPlayer.changmode(mode);
		main_tv_mode.setText("�������ģʽ");
		Toast.makeText(this, "�������ģʽ����", 0).show();
	}

	/**
	 * ˳�򲥷�
	 * Order
	 * @param v
	 */
	public void bt_click_sxbf(View v) {
		mode = MyConstant.MODE_Order;
		MyMediaPlayer.changmode(mode);
		main_tv_mode.setText("˳�򲥷�ģʽ");
		Toast.makeText(this, "˳�򲥷�ģʽ����", 0).show();
	}
}